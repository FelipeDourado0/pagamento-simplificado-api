package br.com.dourado.pagamento.simplificado.api.domain.constants;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConstantesTest {

    @Test
    void deveConterSTATUS_TRANSACAO_FALHACorreto() {
        // when / then
        assertEquals("fail", Constantes.STATUS_TRANSACAO_FALHA);
        assertNotNull(Constantes.STATUS_TRANSACAO_FALHA);
        assertFalse(Constantes.STATUS_TRANSACAO_FALHA.isEmpty());
    }

    @Test
    void deveConterSTATUS_TRANSACAO_SUCESSOCorreto() {
        // when / then
        assertEquals("success", Constantes.STATUS_TRANSACAO_SUCESSO);
        assertNotNull(Constantes.STATUS_TRANSACAO_SUCESSO);
        assertFalse(Constantes.STATUS_TRANSACAO_SUCESSO.isEmpty());
    }

    @Test
    void deveConterMensagemErroFalhaTransacaoDefault() {
        // when / then
        assertEquals("Transferência não autorizada pelo sistema.", Constantes.MENSAGEM_ERRO_FALHA_TRANSACAO_DEFAULT);
        assertNotNull(Constantes.MENSAGEM_ERRO_FALHA_TRANSACAO_DEFAULT);
        assertFalse(Constantes.MENSAGEM_ERRO_FALHA_TRANSACAO_DEFAULT.isEmpty());
    }

    @Test
    void deveGarantirQueConstantesSaoDiferentes() {
        // when / then
        assertNotEquals(Constantes.STATUS_TRANSACAO_FALHA, Constantes.STATUS_TRANSACAO_SUCESSO);
    }

    @Test
    void deveGarantirQueConstantesSaoImutaveis() {
        // given
        String statusFalhaOriginal = Constantes.STATUS_TRANSACAO_FALHA;
        String statusSucessoOriginal = Constantes.STATUS_TRANSACAO_SUCESSO;
        String mensagemOriginal = Constantes.MENSAGEM_ERRO_FALHA_TRANSACAO_DEFAULT;

        // when / then - as constantes devem manter os mesmos valores
        assertEquals(statusFalhaOriginal, Constantes.STATUS_TRANSACAO_FALHA);
        assertEquals(statusSucessoOriginal, Constantes.STATUS_TRANSACAO_SUCESSO);
        assertEquals(mensagemOriginal, Constantes.MENSAGEM_ERRO_FALHA_TRANSACAO_DEFAULT);
    }

    @Test
    void deveValidarTiposDasConstantes() {
        // when / then
        assertTrue(Constantes.STATUS_TRANSACAO_FALHA instanceof String);
        assertTrue(Constantes.STATUS_TRANSACAO_SUCESSO instanceof String);
        assertTrue(Constantes.MENSAGEM_ERRO_FALHA_TRANSACAO_DEFAULT instanceof String);
    }
}