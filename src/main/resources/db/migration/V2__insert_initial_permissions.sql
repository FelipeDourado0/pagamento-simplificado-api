-- V2__insert_initial_permissions.sql
SET search_path TO pagamento_simplificado;

-- Insere permissões iniciais; não adiciona duplicatas se o código já existir
INSERT INTO permissao (role, descricao, dt_criacao)
VALUES
  ('LOJISTA', 'Permissão para usuários do tipo lojista', now()),
  ('CLIENTE', 'Permissão para usuários do tipo cliente', now())
ON CONFLICT (role) DO NOTHING;