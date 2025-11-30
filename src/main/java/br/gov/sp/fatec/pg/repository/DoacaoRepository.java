package br.gov.sp.fatec.pg.repository;

import br.gov.sp.fatec.pg.database.SQLiteConnection;
import br.gov.sp.fatec.pg.model.Doacao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositório para gerenciar as Doações (CRUD).
 */
public class DoacaoRepository {

    /**
     * Registra uma nova doação vinculada a um voluntário.
     */
    public static void add(Doacao doacao) throws Exception {
        String sql = "INSERT INTO doacoes(descricao, recebido, userId) VALUES(?, ?, ?)";
        try (Connection conn = SQLiteConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, doacao.getDescricao());
            pstmt.setBoolean(2, doacao.isRecebido());
            pstmt.setInt(3, doacao.getUserId());
            pstmt.executeUpdate();
        }
    }

    /**
     * Busca apenas as doações de um usuário específico (Visão do Voluntário).
     */
    public static List<Doacao> getByUserId(int userId) throws Exception {
        String sql = "SELECT * FROM doacoes WHERE userId = ?";
        List<Doacao> lista = new ArrayList<>();
        try (Connection conn = SQLiteConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Doacao d = new Doacao();
                d.setId(rs.getInt("id"));
                d.setDescricao(rs.getString("descricao"));
                d.setRecebido(rs.getBoolean("recebido"));
                d.setUserId(rs.getInt("userId"));
                lista.add(d);
            }
        }
        return lista;
    }

    /**
     * Busca TODAS as doações do sistema (Visão do Administrador).
     */
    public static List<Doacao> getAll() throws Exception {
        String sql = "SELECT * FROM doacoes";
        List<Doacao> lista = new ArrayList<>();
        try (Connection conn = SQLiteConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Doacao d = new Doacao();
                d.setId(rs.getInt("id"));
                d.setDescricao(rs.getString("descricao"));
                d.setRecebido(rs.getBoolean("recebido"));
                d.setUserId(rs.getInt("userId"));
                lista.add(d);
            }
        }
        return lista;
    }

    /**
     * Atualiza o status da doação (ex: Marcar como Recebido).
     */
    public static void update(Doacao doacao) throws Exception {
        String sql = "UPDATE doacoes SET descricao = ?, recebido = ? WHERE id = ?";
        try (Connection conn = SQLiteConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, doacao.getDescricao());
            pstmt.setBoolean(2, doacao.isRecebido());
            pstmt.setInt(3, doacao.getId());
            pstmt.executeUpdate();
        }
    }

    /**
     * Exclui um registro de doação.
     */
    public static boolean delete(int id) throws Exception {
        String sql = "DELETE FROM doacoes WHERE id = ?";
        try (Connection conn = SQLiteConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        }
    }
}