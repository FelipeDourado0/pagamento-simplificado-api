-- ./initdb/V01_create_schema.sql
-- Rodará apenas na primeira inicialização do container (quando o volume pgdata estiver vazio)

-- Cria extensão (opcional, necessária se usar gen_random_uuid)
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- Cria schema do projeto
CREATE SCHEMA IF NOT EXISTS pagamento_simplificado AUTHORIZATION myuser;

-- Define search_path para o schema (opcional)
ALTER DATABASE pagamento_simplificado SET search_path = pagamento_simplificado;