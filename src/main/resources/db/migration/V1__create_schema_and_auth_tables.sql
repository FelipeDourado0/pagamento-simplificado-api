-- V1__create_schema_and_auth_tables.sql
CREATE SCHEMA IF NOT EXISTS pagamento_simplificado AUTHORIZATION myuser;
SET search_path TO pagamento_simplificado;

-- Tabelas de autenticação e permissões

CREATE TABLE IF NOT EXISTS permissao (
    id BIGSERIAL PRIMARY KEY,
    codigo VARCHAR(100) NOT NULL UNIQUE,
    descricao TEXT,
    dt_criacao TIMESTAMP WITH TIME ZONE DEFAULT now()
);

CREATE TABLE IF NOT EXISTS perfil (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL UNIQUE,
    descricao TEXT,
    dt_criacao TIMESTAMP WITH TIME ZONE DEFAULT now()
);

CREATE TABLE IF NOT EXISTS perfil_permissao (
    perfil_id BIGINT NOT NULL REFERENCES perfil(id) ON DELETE CASCADE,
    permissao_id BIGINT NOT NULL REFERENCES permissao(id) ON DELETE CASCADE,
    dt_criacao TIMESTAMP WITH TIME ZONE DEFAULT now(),
    PRIMARY KEY (perfil_id, permissao_id)
);

CREATE TABLE IF NOT EXISTS usuario (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(150) NOT NULL UNIQUE,
    email VARCHAR(255) UNIQUE,
    cpf_cnpj VARCHAR(20) UNIQUE,
    pessoa_juridica BOOLEAN NOT NULL DEFAULT false,
    senha VARCHAR(255) NOT NULL,
    perfil_id BIGINT NOT NULL REFERENCES perfil(id),
    ativo BOOLEAN NOT NULL DEFAULT true,
    dt_criacao TIMESTAMP WITH TIME ZONE DEFAULT now()
);

CREATE TABLE IF NOT EXISTS conta_corrente (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL REFERENCES usuario(id),
    saldo NUMERIC(19, 2) NOT NULL DEFAULT 0.00,
    dt_criacao TIMESTAMP WITH TIME ZONE DEFAULT now()
);

CREATE TABLE IF NOT EXISTS historico_transacao (
    id BIGSERIAL PRIMARY KEY,
    conta_origem_id BIGINT NOT NULL REFERENCES conta_corrente(id),
    conta_destino_id BIGINT NOT NULL REFERENCES conta_corrente(id),
    dt_envio_transacao TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    transacao_concluida BOOLEAN NOT NULL DEFAULT false,
    mensagem_erro TEXT,
    descricao TEXT,
    dt_criacao TIMESTAMP WITH TIME ZONE DEFAULT now(),
    CONSTRAINT chk_conta_origem_destino CHECK (conta_origem_id <> conta_destino_id)
);

-- Índices auxiliares
CREATE INDEX IF NOT EXISTS idx_usuario_perfil_id ON usuario(perfil_id);
CREATE INDEX IF NOT EXISTS idx_perfil_permissao_permissao_id ON perfil_permissao(permissao_id);
CREATE INDEX IF NOT EXISTS idx_conta_corrente_usuario_id ON conta_corrente(usuario_id);
CREATE INDEX IF NOT EXISTS idx_historico_transacao_origem ON historico_transacao(conta_origem_id);
CREATE INDEX IF NOT EXISTS idx_historico_transacao_destino ON historico_transacao(conta_destino_id);