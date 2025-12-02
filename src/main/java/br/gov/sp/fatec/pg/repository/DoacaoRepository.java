package br.gov.sp.fatec.pg.repository;

import br.gov.sp.fatec.pg.database.SQLiteConnection;
import br.gov.sp.fatec.pg.model.Doacao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositório de Doações.
 * Responsável pelo CRUD (Create, Read, Update, Delete) dos itens doados.
 * É aqui que gerenciamos a logística (Quantidade e Destino).
 */
public class DoacaoRepository {

    /**
     * Salva uma nova doação no banco de dados.
     * Recebe o objeto Doacao preenchido pelo formulário do site.
     */
    public static void add(Doacao d) throws Exception {
        // SQL atualizado para incluir as 5 colunas, incluindo as de logística
        String sql = "INSERT INTO doacoes(descricao, quantidade, destino, recebido, userId) VALUES(?, ?, ?, ?, ?)";
        
        // Abre conexão e prepara o comando seguro (PreparedStatement evita hacks)
        try (Connection conn = SQLiteConnection.connect(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, d.getDescricao()); // O que é (ex: Arroz)
            ps.setString(2, d.getQuantidade()); // Quanto é (ex: 5kg)
            ps.setString(3, d.getDestino());    // Pra onde vai (ex: Sede)
            ps.setBoolean(4, d.isRecebido());   // Status (Pendente/Recebido)
            ps.setInt(5, d.getUserId());        // Quem cadastrou (Vínculo)
            
            ps.executeUpdate(); // Envia para o banco
        }
    }

    /**
     * Busca doações filtradas por usuário.
     * Garante a privacidade: o Voluntário só vê o que ele registrou.
     */
    public static List<Doacao> getByUserId(int uid) throws Exception {
        List<Doacao> list = new ArrayList<>();
        String sql = "SELECT * FROM doacoes WHERE userId = ?";
        
        try (Connection conn = SQLiteConnection.connect(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, uid); // Filtra pelo ID do usuário logado
            ResultSet rs = ps.executeQuery();
            
            // Enquanto tiver resultados, transforma em objetos e adiciona na lista
            while(rs.next()) list.add(map(rs));
        } 
        return list;
    }

    /**
     * Busca TODAS as doações do sistema.
     * Usado pelo Painel do Administrador para ter visão global do estoque.
     */
    public static List<Doacao> getAll() throws Exception {
        List<Doacao> list = new ArrayList<>();
        
        try (Connection conn = SQLiteConnection.connect(); 
             ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM doacoes")) {
            
            // Varre o banco inteiro e traz tudo
            while(rs.next()) list.add(map(rs));
        } 
        return list;
    }

    /**
     * Atualiza uma doação existente.
     * Usado para corrigir dados ou mudar o status para "Recebido".
     */
    public static void update(Doacao d) throws Exception {
        String sql = "UPDATE doacoes SET descricao=?, quantidade=?, destino=?, recebido=? WHERE id=?";
        
        try (Connection conn = SQLiteConnection.connect(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, d.getDescricao());
            ps.setString(2, d.getQuantidade());
            ps.setString(3, d.getDestino());
            ps.setBoolean(4, d.isRecebido());
            ps.setInt(5, d.getId()); // Usa o ID para saber qual linha alterar
            
            ps.executeUpdate();
        }
    }

    /**
     * Remove uma doação do sistema.
     */
    public static boolean delete(int id) throws Exception {
        try (Connection conn = SQLiteConnection.connect(); 
             PreparedStatement ps = conn.prepareStatement("DELETE FROM doacoes WHERE id=?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0; // Retorna verdadeiro se apagou algo
        }
    }

    /**
     * Método Auxiliar (Helper) de Mapeamento.
     * Transforma a "linha crua" do banco (ResultSet) em um Objeto Java (Doacao).
     * Evita ter que repetir esse bloco de código gigante em todos os métodos de busca.
     */
    private static Doacao map(ResultSet rs) throws SQLException {
        Doacao d = new Doacao();
        d.setId(rs.getInt("id"));
        d.setDescricao(rs.getString("descricao"));
        d.setQuantidade(rs.getString("quantidade")); // Lê o campo novo
        d.setDestino(rs.getString("destino"));       // Lê o campo novo
        d.setRecebido(rs.getBoolean("recebido"));
        d.setUserId(rs.getInt("userId"));
        return d;
    }
}