package br.gov.sp.fatec.pg.database;   // Define o pacote onde essa classe está localizada

import java.sql.Connection;            // Importa a classe de conexão do JDBC
import java.sql.DriverManager;         // Importa o gerenciador que cria conexões com o banco
import java.sql.Statement;             // Importa a classe usada para executar comandos SQL

/**
 * Classe responsável pela conexão com o Banco de Dados SQLite.
 * Gerencia a criação do arquivo do banco e a estrutura das tabelas.
 */
public class SQLiteConnection {


    public static void main(String[] args) {
    try {
        System.out.println("Iniciando teste da conexão SQLite...");
        createTables();
        System.out.println("✓ Teste concluído: Banco criado e tabelas verificadas!");
    } catch (Exception e) {
        System.out.println("✗ Erro ao testar conexão:");
        e.printStackTrace();
    }
}


    // Caminho do arquivo SQLite que será criado no diretório raiz do projeto.
    // "jdbc:sqlite:" indica para o Driver que será usado SQLite.
    private static final String URL = "jdbc:sqlite:ong.db";

    /**
     * Método responsável por abrir a conexão com o banco de dados.
     * @return Retorna um objeto Connection já conectado ao banco.
     * @throws Exception Caso o driver não exista ou o arquivo não possa ser lido.
     */
    public static Connection connect() throws Exception {
        return DriverManager.getConnection(URL); // Cria e retorna a conexão
    }

    /**
     * Método chamado ao iniciar a aplicação para garantir que todas as tabelas existam.
     * Caso não existam, o SQLite cria automaticamente.
     */
    public static void createTables() {

        // SQL para criar a tabela de Usuários
        String sqlUsers = "CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +  // ID automático
                "username TEXT UNIQUE NOT NULL, " +         // Usuário único (não pode repetir)
                "password TEXT NOT NULL, " +                // Senha criptografada
                "role TEXT NOT NULL DEFAULT 'USER', " +     // Cargo do usuário (USER / ADMIN)
                "token TEXT);";                             // Token para manter sessão

        // SQL para criar a tabela de Doações
        String sqlDoacoes = "CREATE TABLE IF NOT EXISTS doacoes (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +  // ID automático
                "descricao TEXT NOT NULL, " +               // Descrição do item doado
                "recebido BOOLEAN DEFAULT FALSE, " +        // Flag: recebido ou não
                "userId INTEGER NOT NULL, " +               // FK para o usuário
                "FOREIGN KEY (userId) REFERENCES users(id));"; // Chave estrangeira ligando tabelas

        // Tenta criar as tabelas
        try (Connection conn = connect();             // Abre a conexão
             Statement stmt = conn.createStatement()) // Cria um executor de SQL
        {
            stmt.execute(sqlUsers);    // Executa criação da tabela users
            stmt.execute(sqlDoacoes);  // Executa criação da tabela doacoes
            System.out.println("Banco de dados ONG configurado e tabelas verificadas.");
        } catch (Exception e) {
            System.err.println("Erro ao criar tabelas: " + e.getMessage()); // Exibe erro
            e.printStackTrace();                                            // Mostra detalhes
        }
    }
}
