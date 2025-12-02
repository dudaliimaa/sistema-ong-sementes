package br.gov.sp.fatec.pg.repository;

import br.gov.sp.fatec.pg.database.SQLiteConnection;
import br.gov.sp.fatec.pg.model.Role;
import br.gov.sp.fatec.pg.model.User;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositório de Usuários.
 * Responsável por salvar, buscar e validar usuários no banco de dados.
 * Aqui está concentrada a lógica de SEGURANÇA (Criptografia).
 */
public class UserRepository {

    /**
     * Cadastra um novo usuário.
     * IMPORTANTE: A senha é criptografada com BCrypt antes de ser salva.
     * Nunca salvamos a senha "crua" no banco por segurança.
     */
    public static void add(User user) throws Exception {
        // Gera o Hash da senha (transforma "123456" em um código ilegível)
        String hash = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        
        String sql = "INSERT INTO users(username, password, role) VALUES(?, ?, ?)";
        
        try (Connection conn = SQLiteConnection.connect(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, hash); // Salva a versão criptografada
            ps.setString(3, user.getRole().toString());
            ps.executeUpdate();
        }
    }

    /**
     * Valida o Login.
     * Busca a senha criptografada no banco e compara com a senha digitada.
     */
    public static boolean validate(String username, String password) throws Exception {
        try (Connection conn = SQLiteConnection.connect(); 
             PreparedStatement ps = conn.prepareStatement("SELECT password FROM users WHERE username = ?")) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            
            // Se achou o usuário, usa o BCrypt para verificar se a senha bate
            if (rs.next()) return BCrypt.checkpw(password, rs.getString("password"));
        }
        return false;
    }

    /**
     * Busca os dados completos do usuário (ID, Role) após o login.
     * Necessário para o sistema saber se é ADMIN ou USER.
     */
    public static User getByUsername(String username) throws Exception {
        try (Connection conn = SQLiteConnection.connect(); 
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE username = ?")) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return map(rs); // Converte a linha do banco em Objeto User
        }
        return null;
    }

    /**
     * Salva o Token de Sessão (UUID) no banco.
     * Isso permite que o usuário continue logado enquanto navega.
     */
    public static void updateToken(String username, String token) throws Exception {
        try (Connection conn = SQLiteConnection.connect(); 
             PreparedStatement ps = conn.prepareStatement("UPDATE users SET token = ? WHERE username = ?")) {
            ps.setString(1, token);
            ps.setString(2, username);
            ps.executeUpdate();
        }
    }

    /**
     * "O Porteiro": Verifica quem é o dono do Token que chegou na requisição.
     * Usado em todas as rotas protegidas para autorizar o acesso.
     */
    public static User getUserByToken(String token) throws Exception {
        try (Connection conn = SQLiteConnection.connect(); 
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE token = ?")) {
            ps.setString(1, token);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return map(rs);
        }
        return null;
    }

    /**
     * Logout: Apaga o token do banco, invalidando a sessão.
     */
    public static void removeToken(String token) throws Exception {
        try (Connection conn = SQLiteConnection.connect(); 
             PreparedStatement ps = conn.prepareStatement("UPDATE users SET token = NULL WHERE token = ?")) {
            ps.setString(1, token);
            ps.executeUpdate();
        }
    }

    /**
     * Lista todos os usuários (Funcionalidade exclusiva do painel ADMIN).
     */
    public static List<User> getAllUsers() throws Exception {
        List<User> list = new ArrayList<>();
        try (Connection conn = SQLiteConnection.connect(); 
             ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM users")) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    /**
     * Remove um usuário do sistema (Funcionalidade exclusiva do painel ADMIN).
     */
    public static boolean delete(String username) throws Exception {
        try (Connection conn = SQLiteConnection.connect(); 
             PreparedStatement ps = conn.prepareStatement("DELETE FROM users WHERE username = ?")) {
            ps.setString(1, username);
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Método Auxiliar (Helper).
     * Evita repetir código de conversão (Banco -> Objeto Java) em todos os métodos acima.
     */
    private static User map(ResultSet rs) throws SQLException {
        User u = new User();
        u.setId(rs.getInt("id"));
        u.setUsername(rs.getString("username"));
        u.setRole(Role.valueOf(rs.getString("role")));
        return u;
    }
}