ALTER TABLE pagamento_simplificado.historico_transacao
ADD COLUMN valor NUMERIC(19, 4);

ALTER TABLE conta_corrente
ADD COLUMN agencia VARCHAR(20) NOT NULL,
ADD COLUMN conta_corrente VARCHAR(30) NOT NULL;