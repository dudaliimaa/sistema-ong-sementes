package br.gov.sp.fatec.pg.model;

/**
 * Classe Modelo que representa uma Doação no sistema.
 */
public class Doacao {
    
    //Chave Primária
    private Integer id;
    
    private String descricao;
    
    private String quantidade; 
    
    private String destino;    
    
    // Status: false = Pendente, true = Recebido no Estoque
    private Boolean recebido;
    
    // ID do voluntário que cadastrou (Chave Estrangeira)
    private Integer userId;

    public Doacao() {}

    // Construtor
    public Doacao(String descricao, String quantidade, String destino, Integer userId) {
        this.descricao = descricao;
        this.quantidade = quantidade;
        this.destino = destino;
        this.userId = userId;
        this.recebido = false; // Padrão: Inicia como não recebido
    }

    // Getters e Setters
    public Integer getId() { 
        return id; 
    }
    public void setId(Integer id) { 
        this.id = id; 
    }
    
    public String getDescricao() { 
        return descricao; 
    }
    public void setDescricao(String descricao) { 
        this.descricao = descricao; 
    }
    
    // Getters e Setters para os novos campos
    public String getQuantidade() { 
        return quantidade; 
    }
    public void setQuantidade(String quantidade) { 
        this.quantidade = quantidade; 
    }
    
    public String getDestino() { 
        return destino; 
    }
    public void setDestino(String destino) { 
        this.destino = destino; 
    }
    
    public Boolean isRecebido() { 
        return recebido; 
    }
    public void setRecebido(Boolean recebido) { 
        this.recebido = recebido; 
    }
    
    public Integer getUserId() { 
        return userId; 
    }
    public void setUserId(Integer userId) { 
        this.userId = userId; 
    }
}