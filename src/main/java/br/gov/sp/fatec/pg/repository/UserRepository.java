package br.gov.sp.fatec.pg.repository;

import br.gov.sp.fatec.pg.database.SQLiteConnection;
import br.gov.sp.fatec.pg.model.Role;
import br.gov.sp.fatec.pg.model.User;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositório responsável pela persistência de usuários.
 * Gerencia criptografia de senhas e validação de tokens de sessão.
 */
public class UserRepository {

    /**
     * Cadastra um novo usuário no banco de dados.
     * A senha é criptografada com BCrypt antes de ser salva para segurança.
     */
    public static void add(User user) throws Exception {
        // Gera um hash seguro da senha (nunca salvamos a senha pura)
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());

        String sql = "INSERT INTO users(username, password, role) VALUES(?, ?, ?)";

        // Uso de try-with-resources para garantir o fechamento da conexão
        try (Connection conn = SQLiteConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, hashedPassword); // Salva o hash
            pstmt.setString(3, user.getRole().toString());

            pstmt.executeUpdate();
        }
    }

    /**
     * Verifica se o usuário e senha correspondem a um registro válido.
     */
    public static boolean validate(String username, String password) throws Exception {
        String sql = "SELECT password FROM users WHERE username = ?";
        try (Connection conn = SQLiteConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("password");
                // Compara a senha digitada com o hash salvo no banco
                return BCrypt.checkpw(password, storedHash);
            }
        }
        return false;
    }

    /**
     * Busca os dados completos de um usuário pelo nome (usado no login).
     */
    public static User getByUsername(String username) throws Exception {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = SQLiteConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                User u = new User();
                u.setId(rs.getInt("id"));
                u.setUsername(rs.getString("username"));
                u.setRole(Role.valueOf(rs.getString("role")));
                return u;
            }
        }
        return null;
    }

    /**
     * Atualiza o token de sessão ao realizar login.
     */
    public static void updateToken(String username, String token) throws Exception {
        String sql = "UPDATE users SET token = ? WHERE username = ?";
        try (Connection conn = SQLiteConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, token);
            pstmt.setString(2, username);
            pstmt.executeUpdate();
        }
    }

    /**
     * Valida quem é o usuário dono de um determinado token (Middleware de Auth).
     */
    public static User getUserByToken(String token) throws Exception {
        String sql = "SELECT * FROM users WHERE token = ?";
        try (Connection conn = SQLiteConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, token);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                User u = new User();
                u.setId(rs.getInt("id"));
                u.setUsername(rs.getString("username"));
                u.setRole(Role.valueOf(rs.getString("role")));
                u.setToken(rs.getString("token"));
                return u;
            }
        }
        return null;
    }

    /**
     * Remove o token ao fazer logout.
     */
    public static void removeToken(String token) throws Exception {
        String sql = "UPDATE users SET token = NULL WHERE token = ?";
        try (Connection conn = SQLiteConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, token);
            pstmt.executeUpdate();
        }
    }

    /**
     * Lista todos os usuários cadastrados (Funcionalidade Admin).
     */
    public static List<User> getAllUsers() throws Exception {
        List<User> list = new ArrayList<>();
        String sql = "SELECT id, username, role FROM users";
        try (Connection conn = SQLiteConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                User u = new User();
                u.setId(rs.getInt("id"));
                u.setUsername(rs.getString("username"));
                u.setRole(Role.valueOf(rs.getString("role")));
                list.add(u);
            }
        }
        return list;
    }

    /**
     * Remove um usuário do sistema (Funcionalidade Admin).
     */
    public static boolean delete(String username) throws Exception {
        String sql = "DELETE FROM users WHERE username = ?";
        try (Connection conn = SQLiteConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            return pstmt.executeUpdate() > 0;
        }
    }
}
