package br.gov.sp.fatec.pg;

import br.gov.sp.fatec.pg.database.SQLiteConnection;
import br.gov.sp.fatec.pg.model.Role;
import br.gov.sp.fatec.pg.model.Doacao;
import br.gov.sp.fatec.pg.model.User;
import br.gov.sp.fatec.pg.repository.DoacaoRepository;
import br.gov.sp.fatec.pg.repository.UserRepository;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        // 1. Inicializa o Banco de Dados
        SQLiteConnection.createTables();

        // 2. Garante um ADMIN padrão se o banco estiver vazio
        try {
            List<User> users = UserRepository.getAllUsers();
            if (users.isEmpty()) {
                User admin = new User("admin", "admin123", Role.ADMIN);
                UserRepository.add(admin);
                System.out.println("--- ADMIN PADRÃO CRIADO (admin/admin123) ---");
            }
        } catch (Exception e) {
            System.err.println("Erro ao verificar admin: " + e.getMessage());
        }

        // 3. Configura o Javalin (Servidor Web)
        Javalin app = Javalin.create(config -> {
            config.http.defaultContentType = "application/json";
            // Define a pasta do Frontend
            config.staticFiles.add("/static", Location.CLASSPATH);
            
            // Ajuste de acentuação
            config.pvt.javaLangErrorHandler((res, error) -> res.setCharacterEncoding("UTF-8"));
        }).start(7078);

        // 4. SEGURANÇA (Middlewares)
        
        // Verifica Token em todas as rotas /api/*
        app.before("/api/*", ctx -> {
            try {
                String token = ctx.header("Authorization");
                if (token == null || token.isEmpty()) {
                    ctx.status(401).json(Map.of("error", "Não autorizado: Token ausente"));
                    ctx.skipRemainingHandlers();
                    return;
                }
                User user = UserRepository.getUserByToken(token);
                if (user == null) {
                    ctx.status(401).json(Map.of("error", "Sessão expirada/inválida"));
                    ctx.skipRemainingHandlers();
                    return;
                }
                ctx.attribute("user", user); // Salva usuário na memória da requisição
            } catch (Exception e) {
                ctx.status(500).json(Map.of("error", "Erro de Auth: " + e.getMessage()));
            }
        });

        // Protege área Admin
        app.before("/api/admin/*", ctx -> {
            User user = ctx.attribute("user");
            if (user == null || user.getRole() != Role.ADMIN) {
                ctx.status(403).json(Map.of("error", "Acesso Negado: Requer Admin"));
                ctx.skipRemainingHandlers();
            }
        });

        // 5. ROTAS (Endpoints)

        // Login
        app.post("/login", ctx -> {
            try {
                User login = ctx.bodyAsClass(User.class);
                if (UserRepository.validate(login.getUsername(), login.getPassword())) {
                    User realUser = UserRepository.getByUsername(login.getUsername());
                    String token = UUID.randomUUID().toString();
                    UserRepository.updateToken(realUser.getUsername(), token);
                    
                    // Retorna token e role para o frontend saber como se comportar
                    ctx.json(Map.of(
                        "token", token,
                        "username", realUser.getUsername(),
                        "role", realUser.getRole(),
                        "id", realUser.getId()
                    ));
                } else {
                    ctx.status(401).json(Map.of("error", "Dados incorretos"));
                }
            } catch (Exception e) {
                ctx.status(500).json(Map.of("error", "Erro Login: " + e.getMessage()));
            }
        });

        // Cadastro com Validação de Chave Mestra
        app.post("/signup", ctx -> {
            try {
                User newUser = ctx.bodyAsClass(User.class);
                
                // LÓGICA DE SEGURANÇA:
                // Se o código for a senha mestra, vira ADMIN. Senão, é VOLUNTÁRIO.
                if ("ONG-MASTER-2025".equals(newUser.getAdminCode())) {
                    newUser.setRole(Role.ADMIN);
                } else {
                    newUser.setRole(Role.USER);
                }
                
                if(UserRepository.validate(newUser.getUsername(), "")) {
                     ctx.status(400).json(Map.of("error", "Usuário já existe"));
                     return;
                }
                
                UserRepository.add(newUser);
                ctx.status(201).json(Map.of("message", "Cadastro realizado com sucesso!"));
            } catch (Exception e) {
                ctx.status(500).json(Map.of("error", "Erro ao cadastrar: " + e.getMessage()));
            }
        });

        // Logout
        app.put("/logout", ctx -> {
            String token = ctx.header("Authorization");
            if (token != null) UserRepository.removeToken(token);
            ctx.status(204);
        });

        // Doações (Voluntário)
        app.get("/api/doacoes", ctx -> {
            User user = ctx.attribute("user");
            ctx.json(DoacaoRepository.getByUserId(user.getId()));
        });

        app.post("/api/doacoes", ctx -> {
            User user = ctx.attribute("user");
            Doacao d = ctx.bodyAsClass(Doacao.class);
            d.setUserId(user.getId());
            d.setRecebido(false);
            DoacaoRepository.add(d);
            ctx.status(201).json(Map.of("message", "Doação Salva"));
        });

        // Doações (Admin)
        app.get("/api/admin/doacoes", ctx -> {
            ctx.json(DoacaoRepository.getAll());
        });

        app.put("/api/doacoes/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Doacao d = ctx.bodyAsClass(Doacao.class);
            d.setId(id);
            DoacaoRepository.update(d); // Atualiza status
            ctx.status(200).json(Map.of("message", "Atualizado"));
        });

        app.delete("/api/doacoes/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            DoacaoRepository.delete(id);
            ctx.status(204);
        });

        // Gestão de Equipe (Admin)
        app.get("/api/admin/users", ctx -> ctx.json(UserRepository.getAllUsers()));
        
        app.post("/api/admin/users", ctx -> {
            UserRepository.add(ctx.bodyAsClass(User.class));
            ctx.status(201).json(Map.of("message", "Usuário criado"));
        });
        
        app.delete("/api/admin/users/{username}", ctx -> {
            UserRepository.delete(ctx.pathParam("username"));
            ctx.status(204);
        });

        System.out.println("Sistema Rodando! Acesse: http://localhost:7078");
    }
}
