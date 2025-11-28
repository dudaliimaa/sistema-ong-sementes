package br.gov.sp.fatec.pg.model;

/**
 * Classe que representa a entidade Doação no sistema.
 * Armazena os dados do item doado e o vínculo com o voluntário que registrou.
 */
public class Doacao {
    // Identificador único da doação (Gerado pelo Banco de Dados)
    private Integer id;
    
    // Descrição do item (Ex: "Cesta Básica", "Roupas")
    private String descricao;
    
    // Status da doação: false = Pendente de coleta, true = Recebido na ONG
    private Boolean recebido;
    
    // ID do voluntário responsável (Chave Estrangeira)
    private Integer userId;

    // Construtor vazio (necessário para serialização JSON)
    public Doacao() {}

    // Construtor para criar uma nova doação
    public Doacao(String descricao, Integer userId) {
        this.descricao = descricao;
        this.userId = userId;
        this.recebido = false; // Padrão: inicia como pendente
    }

    // Métodos de Encapsulamento (Getters e Setters)

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Boolean isRecebido() { return recebido; }
    public void setRecebido(Boolean recebido) { this.recebido = recebido; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
}