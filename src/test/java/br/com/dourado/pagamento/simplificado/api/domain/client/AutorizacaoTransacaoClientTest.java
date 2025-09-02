package br.com.dourado.pagamento.simplificado.api.domain.client;

import br.com.dourado.pagamento.simplificado.api.domain.dtos.autorizacaoTransacao.AutorizacaoResponseDTO;
import br.com.dourado.pagamento.simplificado.api.domain.dtos.autorizacaoTransacao.AutorizacaoTransacaoResponseDTO;
import br.com.dourado.pagamento.simplificado.api.infra.helper.LoggerHelper;
import br.com.dourado.pagamento.simplificado.api.infra.helper.WebClientHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AutorizacaoTransacaoClientTest {

    @Mock
    private LoggerHelper loggerHelper;

    @Mock
    private WebClientHelper webClientHelper;

    @InjectMocks
    private AutorizacaoTransacaoClient autorizacaoTransacaoClient;

    private String url;
    private AutorizacaoTransacaoResponseDTO responseAutorizado;
    private AutorizacaoTransacaoResponseDTO responseNegado;

    @BeforeEach
    void setUp() {
        url = "https://util.devi.tools/api/v2/authorize";

        // Response autorizado
        AutorizacaoResponseDTO dataAutorizado = new AutorizacaoResponseDTO(true);
        responseAutorizado = new AutorizacaoTransacaoResponseDTO("success", dataAutorizado);

        // Response negado
        AutorizacaoResponseDTO dataNegado = new AutorizacaoResponseDTO(false);
        responseNegado = new AutorizacaoTransacaoResponseDTO("success", dataNegado);
    }

    @Test
    void deveObterAutorizacaoComSucessoQuandoTransacaoForAutorizada() {

        when(webClientHelper.get(any(), any()))
                .thenReturn(Mono.just(responseAutorizado));
        // when
        AutorizacaoTransacaoResponseDTO response = autorizacaoTransacaoClient.obterStatusDeAutorizacaoParaTransacao();

        // then
        assertNotNull(response);
        assertEquals("success", response.getStatus());
        assertNotNull(response.getData());
        assertTrue(response.getData().getAuthorization());

        verify(webClientHelper, times(1)).get(any(), any());
    }

    @Test
    void deveObterAutorizacaoComSucessoQuandoTransacaoForNegada() {

        when(webClientHelper.get(any(), any()))
                .thenReturn(Mono.just(responseNegado));

        // when
        AutorizacaoTransacaoResponseDTO response = autorizacaoTransacaoClient.obterStatusDeAutorizacaoParaTransacao();

        // then
        assertNotNull(response);
        assertEquals("success", response.getStatus());
        assertNotNull(response.getData());
        assertFalse(response.getData().getAuthorization());

        verify(webClientHelper, times(1)).get(any(), any());
    }

    @Test
    void deveRetornarNullQuandoWebClientNaoResponder() {

        when(webClientHelper.get(any(), any()))
                .thenReturn(Mono.empty());

        // when
        AutorizacaoTransacaoResponseDTO response = autorizacaoTransacaoClient.obterStatusDeAutorizacaoParaTransacao();

        // then
        assertNull(response);
        verify(webClientHelper, times(1)).get(any(), any());
    }

    @Test
    void deveChamarWebClientComParametrosCorretos() {

        when(webClientHelper.get(any(), any()))
                .thenReturn(Mono.just(responseAutorizado));

        // when
        autorizacaoTransacaoClient.obterStatusDeAutorizacaoParaTransacao();

        // then
        verify(webClientHelper).get(any(), any());
    }
}