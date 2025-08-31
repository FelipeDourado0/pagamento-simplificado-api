-- V3__insert_initial_profiles.sql
SET search_path TO pagamento_simplificado;

-- Insere perfis iniciais (não duplica caso já existam)
INSERT INTO perfil (nome, dt_criacao)
VALUES
  ('LOJISTA', now()),
  ('CLIENTE', now())
ON CONFLICT (nome) DO NOTHING;

-- Associa perfil LOJISTA à permissão LOJISTA (se ainda não estiver associada)
INSERT INTO perfil_permissao (perfil_id, permissao_id, dt_criacao)
SELECT p.id, pe.id, now()
FROM perfil p
JOIN permissao pe ON pe.role = 'LOJISTA'
WHERE p.nome = 'LOJISTA'
ON CONFLICT (perfil_id, permissao_id) DO NOTHING;

-- Associa perfil CLIENTE à permissão CLIENTE (se ainda não estiver associada)
INSERT INTO perfil_permissao (perfil_id, permissao_id, dt_criacao)
SELECT p.id, pe.id, now()
FROM perfil p
JOIN permissao pe ON pe.role = 'CLIENTE'
WHERE p.nome = 'CLIENTE'
ON CONFLICT (perfil_id, permissao_id) DO NOTHING;