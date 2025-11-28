package br.gov.sp.fatec.pg.model;

/**
 * Classe Modelo de Usuário.
 * Serve tanto para Voluntários quanto para Administradores.
 */
public class User {
    private Integer id;
    
    // Nome de usuário para login (deve ser único)
    private String username;
    
    // Senha do usuário (será armazenada criptografada)
    private String password;
    
    // Define o perfil de acesso (Role)
    private Role role;
    
    // Token de autenticação para manter a sessão ativa (UUID)
    private String token;

    public User() {}

    public User(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Getters e Setters

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}