# Sistema de Gestão - ONG Sementes do Amanhã

Este projeto é uma aplicação **Fullstack Web** desenvolvida como Trabalho Semestral da disciplina de Programação Orientada a Objetos (POO) do curso de Análise e Desenvolvimento de Sistemas (FATEC-PG).

O objetivo é informatizar o processo de captação e gestão de doações, substituindo controles manuais por um sistema seguro e centralizado.

---

## Funcionalidades

### Segurança e Acesso (RBAC)
- **Autenticação Segura:** Login com hash de senha (BCrypt) e Tokens de sessão.
- **Controle de Níveis:**
  - **Voluntário:** Registra e visualiza suas próprias doações.
  - **Coordenador (Admin):** Gerencia equipe, visualiza todas as doações e altera status.
- **Chave Mestra:** Sistema de cadastro especial para administradores via código de segurança.

### Gestão de Doações
- Cadastro rápido de itens doados.
- Controle de status (Pendente / Recebido na ONG).
- Histórico individual por voluntário.

### Interface Moderna (SPA)
- **Single Page Application:** Navegação fluida sem recarregamento de página.
- **Responsivo:** Layout adaptável para mobile e desktop (Bootstrap 5).
- **Interativo:** Feedback visual instantâneo (Alpine.js).

---

## Tecnologias Utilizadas

**Backend:**
- **Java 21**: Linguagem base.
- **Javalin**: Framework web leve para API REST.
- **SQLite + JDBC**: Banco de dados relacional (arquivo `ong.db`).
- **BCrypt**: Biblioteca de criptografia para segurança.
- **Swagger/OpenAPI**: Documentação automática da API.

**Frontend:**
- **HTML5 & CSS3**
- **Bootstrap 5**: Estilização e layout.
- **Alpine.js**: Framework JavaScript leve para reatividade.

---

## Como Executar o Projeto

### Pré-requisitos
- Java JDK 21 instalado.
- Maven instalado (ou via IDE).

### Passo a Passo
1. Clone este repositório:
   ```bash
   git clone [https://github.com/dudaliimaa/sistema-ong-semestres.git](https://github.com/dudaliimaa/sistema-ong-semestres.git)
