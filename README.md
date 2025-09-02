# 💳 Pagamento Simplificado API
### Programador responsável: <a href="https://www.linkedin.com/in/felipe-dourado-643889209/" target="blank">Felipe Dourado</a>

API de pagamento simplificado desenvolvida em **Java + Spring Boot**, com foco em segurança, escalabilidade e boas práticas.  
O sistema implementa:
- cadastro de usuários e contas
- autenticação JWT
- controle de permissões
- fluxo de transferências financeiras com registros históricos.
---

##  Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.x**
  - Spring Web
  - Spring Data JPA (Hibernate)
  - Spring Security (JWT + OAuth2)
- **Banco de Dados:** PostgreSQL
- **Build/Dependency:** Gradle
- **Containerização:** Docker + Docker Compose
- **Documentação:** OpenAPI/Swagger UI
- **Testes:** JUnit 5 + Mockito
- **Logging:** SLF4J com helper customizado

## Ferramentas
- Intellij
- Abacus.AI (Agente de AI)
- Docker Desktop
- DBeaver
- Postman
---

## Arquitetura

- **Camada API (Controller):** expõe os endpoints REST.  
- **Camada Service:** concentra regras de negócio (transferência, cadastro, autenticação).  
- **Camada Repository:** abstração de persistência com Spring Data JPA.  
- **Camada Infra:** segurança, JWT, helpers (WebClient, Logger, etc).  
- **Banco de Dados:** relacionamento normalizado entre Usuário, Conta, Perfil e Histórico de Transações.  

---

## ⚙️ Funcionalidades

###  Autenticação & Autorização
- Registro de usuários com criptografia BCrypt.
- Perfis (`ROLE_CLIENTE`, `ROLE_LOJISTA`) e permissões.
- Geração e validação de tokens JWT.
- Proteção de endpoints com Spring Security.

### Usuário & Conta
- Cadastro e consulta de **Usuários** e **Contas Correntes**.
- Saldo inicial configurável no cadastro.
- Consulta de dados do usuário via CPF/CNPJ.

### Transferências
- Transferência entre contas com validações:
  - Não permite saldo circular (origem igual ao destino);
  - Bloqueio de pessoa jurídica enviando;
  - Restrições de saldo insuficiente.
- Integração com API externa de autorização da transação.
- Registro no **Histórico de Transações**.

### Histórico
- Consulta de transações de um usuário via CPF/CNPJ.
- Estrutura de log detalhada com timestamps.

## Modelo Entidade Relacionamento

<img width="609" height="1245" alt="image" src="https://github.com/user-attachments/assets/11743174-4010-4b87-a5df-3f92c26db91c" />


## Projeto desenvolvido com base no desafio: 
 - https://github.com/PicPay/picpay-desafio-backend?tab=readme-ov-file
