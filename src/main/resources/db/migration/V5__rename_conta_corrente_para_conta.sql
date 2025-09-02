-- Renomeia a tabela
ALTER TABLE pagamento_simplificado.conta_corrente RENAME TO conta;

-- Renomeia a coluna "conta_corrente" para "numero_conta"
ALTER TABLE pagamento_simplificado.conta RENAME COLUMN conta_corrente TO numero_conta;