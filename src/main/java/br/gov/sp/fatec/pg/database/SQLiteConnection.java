package br.gov.sp.fatec.pg.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * Classe responsável pela Infraestrutura do Banco de Dados.
 * Ela gerencia a conexão com o arquivo físico (.db) e cria a estrutura das tabelas (DDL).
 */
public class SQLiteConnection {

    // Define o caminho e o nome do arquivo do banco. 
    // "jdbc:sqlite" diz ao Java qual driver usar.
    private static final String URL = "jdbc:sqlite:ong.db";

    /**
     * Método utilitário para abrir uma conexão.
     * É usado por todos os Repositórios quando precisam salvar ou ler dados.
     */
    public static Connection connect() throws Exception {
        return DriverManager.getConnection(URL);
    }

    /**
     * Método de Inicialização (Setup).
     * É chamado no Main.java assim que o sistema liga.
     * Garante que as tabelas existam antes de qualquer operação.
     */
    public static void createTables() {
        
        // 1. Definição da Tabela de USUÁRIOS
        String sqlUsers = "CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " + // ID automático (1, 2, 3...)
                "username TEXT UNIQUE NOT NULL, " +        // UNIQUE: Não permite dois usuários com mesmo nome
                "password TEXT NOT NULL, " +               // Senha (será salvo o Hash criptografado)
                "role TEXT NOT NULL DEFAULT 'USER', " +    // Nível de acesso (USER ou ADMIN)
                "token TEXT);";                            // Guarda a sessão do usuário logado

        // 2. Definição da Tabela de DOAÇÕES (Com Logística)
        String sqlDoacoes = "CREATE TABLE IF NOT EXISTS doacoes (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "descricao TEXT NOT NULL, " +
                
                // [NOVOS CAMPOS DE LOGÍSTICA]
                "quantidade TEXT, " +  // Ex: "5kg", "2 caixas"
                "destino TEXT, " +     // Ex: "Sede", "Família Silva"
                
                "recebido BOOLEAN DEFAULT FALSE, " + // Status: 0 (Pendente) ou 1 (Entregue)
                "userId INTEGER NOT NULL, " +        // Quem registrou?
                
                // CHAVE ESTRANGEIRA (Foreign Key):
                // Cria um vínculo inquebrável entre a doação e o usuário.
                // Se tentarmos salvar uma doação sem dono, o banco bloqueia.
                "FOREIGN KEY (userId) REFERENCES users(id));";

        // Bloco de Execução
        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            // Envia os comandos SQL para o arquivo do banco
            stmt.execute(sqlUsers);
            stmt.execute(sqlDoacoes);
            System.out.println("Banco de dados configurado com Logística.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}