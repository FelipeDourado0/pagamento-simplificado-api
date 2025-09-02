package br.com.dourado.pagamento.simplificado.api.service;

import br.com.dourado.pagamento.simplificado.api.domain.client.AutorizacaoTransacaoClient;
import br.com.dourado.pagamento.simplificado.api.domain.constants.Constantes;
import br.com.dourado.pagamento.simplificado.api.domain.dtos.autorizacaoTransacao.AutorizacaoResponseDTO;
import br.com.dourado.pagamento.simplificado.api.domain.dtos.autorizacaoTransacao.AutorizacaoTransacaoResponseDTO;
import br.com.dourado.pagamento.simplificado.api.domain.entities.Conta;
import br.com.dourado.pagamento.simplificado.api.domain.entities.HistoricoTransacao;
import br.com.dourado.pagamento.simplificado.api.domain.repositories.HistoricoTransacaoRepository;
import br.com.dourado.pagamento.simplificado.api.infra.exceptions.BadRequestExeption;
import br.com.dourado.pagamento.simplificado.api.infra.helper.LoggerHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.contains;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HistoricoTransacaoServiceTest {

    @Mock
    private HistoricoTransacaoRepository historicoTransacaoRepository;

    @Mock
    private AutorizacaoTransacaoClient autorizacaoTransacaoClient;

    @Mock
    private LoggerHelper loggerHelper;

    @InjectMocks
    private HistoricoTransacaoService historicoTransacaoService;

    private Conta contaOrigem;
    private Conta contaDestino;

    @BeforeEach
    void setUp() {
        contaOrigem = new Conta();
        contaOrigem.setId(1L);
        contaOrigem.setAgencia("0001");
        contaOrigem.setNumeroConta("123456-7");

        contaDestino = new Conta();
        contaDestino.setId(2L);
        contaDestino.setAgencia("0001");
        contaDestino.setNumeroConta("987654-3");
    }

    @Test
    void deveCriarHistoricoComSucessoQuandoAutorizado() {
        // given
        AutorizacaoResponseDTO autorizacao = new AutorizacaoResponseDTO(true);
        AutorizacaoTransacaoResponseDTO responseMock =
                new AutorizacaoTransacaoResponseDTO(Constantes.STATUS_TRANSACAO_SUCESSO, autorizacao);

        when(autorizacaoTransacaoClient.obterStatusDeAutorizacaoParaTransacao())
                .thenReturn(responseMock);

        when(historicoTransacaoRepository.save(any(HistoricoTransacao.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // when
        HistoricoTransacao historico = historicoTransacaoService.criarHistoricoTransacao(
                contaOrigem,
                contaDestino,
                BigDecimal.valueOf(100.0),
                "Transferência PIX"
        );

        // then
        assertNotNull(historico);
        assertEquals(contaOrigem, historico.getContaOrigem());
        assertEquals(contaDestino, historico.getContaDestino());
        assertEquals(BigDecimal.valueOf(100.0), historico.getValor());
        assertEquals("Transferência PIX", historico.getDescricao());
        assertTrue(historico.isTransacaoConcluida());

        verify(historicoTransacaoRepository).save(any(HistoricoTransacao.class));
        verify(historicoTransacaoRepository).flush();
        verify(loggerHelper).info(eq(HistoricoTransacaoService.class), contains("Finalizado criacao historico"));
    }

    @Test
    void deveLancarBadRequestExceptionQuandoStatusFalha() {
        // given
        AutorizacaoResponseDTO autorizacao = new AutorizacaoResponseDTO(false);
        AutorizacaoTransacaoResponseDTO responseMock =
                new AutorizacaoTransacaoResponseDTO(Constantes.STATUS_TRANSACAO_FALHA, autorizacao);

        when(autorizacaoTransacaoClient.obterStatusDeAutorizacaoParaTransacao())
                .thenReturn(responseMock);

        when(historicoTransacaoRepository.save(any(HistoricoTransacao.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // when + then
        BadRequestExeption exception = assertThrows(
                BadRequestExeption.class,
                () -> historicoTransacaoService.criarHistoricoTransacao(
                        contaOrigem,
                        contaDestino,
                        BigDecimal.valueOf(200.0),
                        "Transferência falha"
                )
        );

        assertEquals("Transferência recusada.", exception.getTitulo());
        assertEquals("Transferência negada.", exception.getMenssagem());

        ArgumentCaptor<HistoricoTransacao> captor = ArgumentCaptor.forClass(HistoricoTransacao.class);
        verify(historicoTransacaoRepository).save(captor.capture());

        HistoricoTransacao historicoSalvo = captor.getValue();
        assertFalse(historicoSalvo.isTransacaoConcluida());
        assertEquals(Constantes.MENSAGEM_ERRO_FALHA_TRANSACAO_DEFAULT, historicoSalvo.getMensagemErro());

        verify(loggerHelper).info(eq(HistoricoTransacaoService.class), contains("Transferência negada"));
    }
}