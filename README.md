# 🌱 Portal ONG Sementes do Amanhã

> **Sistema de Gestão Fullstack** para Logística de Doações e Controle de Voluntariado.

Este projeto foi desenvolvido como Trabalho Semestral da disciplina de **Programação Orientada a Objetos (POO)** do curso de Análise e Desenvolvimento de Sistemas (FATEC-PG).

O objetivo é profissionalizar o processo de captação de recursos da ONG, substituindo controles manuais por uma plataforma web segura, centralizada e auditável.

---

## Funcionalidades do Sistema

### 1. Segurança e Acesso (RBAC)
* **Autenticação Robusta:** Login com criptografia de ponta (BCrypt) e sessões baseadas em Tokens UUID.
* **Chave Mestra:** Sistema exclusivo para cadastro autônomo da diretoria utilizando o código de segurança `ONG-MASTER-2025`.
* **Proteção de Rotas:** Middlewares que impedem acesso não autorizado a áreas administrativas.

### 2. Gestão Logística de Doações
* **Registro Detalhado:** Cadastro de itens com **Descrição**, **Quantidade** e **Local de Destino** (Sede ou Família).
* **Fluxo de Status:** Controle visual de itens `Pendente` (com o voluntário) vs `Recebido` (no estoque da ONG).
* **Histórico Individual:** O voluntário visualiza apenas a sua própria captação, garantindo privacidade.

### 3. Administração e Equipe
* **Painel de Controle (Dashboard):** Métricas em tempo real de total arrecadado, pendências e entregas.
* **Gestão de Pessoas:** O Administrador possui controle total para adicionar ou remover membros da equipe.
* **Visão Global:** O Administrador tem acesso ao estoque completo de todos os voluntários para dar baixa nos itens.

### 4. Interface Moderna (SPA)
* **Experiência Fluida:** Navegação instantânea sem recarregamento de página (Single Page Application).
* **Responsividade:** Layout adaptável para celulares e computadores (Bootstrap 5) com Menu Lateral (Sidebar).
* **Perfil do Usuário:** Área para visualização de dados cadastrais e estatísticas pessoais.

---

## Stack Tecnológica

| Camada | Tecnologia | Aplicação |
| :--- | :--- | :--- |
| **Backend** | Java 21 | Linguagem Core |
| **API** | Javalin | Framework Web leve para rotas REST |
| **Persistência** | SQLite + JDBC | Banco de Dados Relacional (Arquivo `ong.db`) |
| **Frontend** | HTML5 + Alpine.js | Interatividade e Lógica Reativa |
| **Estilo** | Bootstrap 5 | Design System e Responsividade |
| **Docs** | Swagger/OpenAPI | Documentação automática dos Endpoints |

---

## Como Executar o Projeto

### Pré-requisitos
* Java JDK 21 instalado.
* Maven instalado (ou via IDE).

### Passo a Passo

1. **Clone o repositório:**
   ```bash
   git clone [https://github.com/dudaliimaa/sistema-ong-sementes.git](https://github.com/dudaliimaa/sistema-ong-sementes.git)

2. **Importe o projeto:** Abra a pasta no **VS Code** (com Extension Pack for Java) ou **IntelliJ/Eclipse**.

3. **Atualize as Dependências:** Aguarde o Maven baixar as bibliotecas listadas no `pom.xml` (Javalin, SQLite, etc).

4. **Execute:** Localize e rode a classe principal: 
`src/main/java/br/gov/sp/fatec/pg/Main.java`

5. **Acesse:** Abra o navegador em: http://localhost:7078

---

## Credenciais para Teste
O sistema gera um administrador padrão na primeira execução, mas você também pode criar novos usuários.
| Perfil | Usuário | Senha | Instrução |
| :--- | :--- | :--- | :--- |
| **Admin Padrão** | `admin` | `admin123` | Acesso total imediato |
| **Criar Admin** | (Seu Nome) | (Sua Senha) | No cadastro, use o código: **`ONG-MASTER-2025`** |
| **Criar Voluntário**| (Seu Nome) | (Sua Senha) | Deixe o código em branco no cadastro |

> **📄 Documentação Técnica:** Para ver as rotas da API, acesse /swagger com o servidor rodando.

---

## 👥 Autores

* Eduarda Lima
* Nathalia Cappellini
* Vitor Lopes
* Mateus Pinheiro

---
<div align="center">
    <small>© 2025 Sementes do Amanhã. Projeto Acadêmico Open Source (FATEC-PG).</small>
</div>