package br.com.dourado.pagamento.simplificado.api.controller;

import br.com.dourado.pagamento.simplificado.api.domain.dtos.transferencia.DadosHistoricoTransferenciaDTO;
import br.com.dourado.pagamento.simplificado.api.domain.dtos.transferencia.DadosTransferenciaUsuarioDTO;
import br.com.dourado.pagamento.simplificado.api.domain.dtos.transferencia.ExtratoTransferenciaDTO;
import br.com.dourado.pagamento.simplificado.api.domain.dtos.transferencia.TransferenciaRequestDTO;
import br.com.dourado.pagamento.simplificado.api.service.TransferenciaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.times;

@ExtendWith(MockitoExtension.class)
class TransferenciaControllerTest {

    @Mock
    private TransferenciaService transferenciaService;

    @InjectMocks
    private TransferenciaController transferenciaController;

    private TransferenciaRequestDTO transferenciaRequest;
    private ExtratoTransferenciaDTO extratoTransferencia;

    @BeforeEach
    void setUp() {
        transferenciaRequest = new TransferenciaRequestDTO();
        transferenciaRequest.setEmailDestino("12345678900");
        transferenciaRequest.setEmailOrigem("98765432100");
        transferenciaRequest.setValorTransferencia(BigDecimal.valueOf(500.00));
        transferenciaRequest.setDescricao("Pagamento de serviço");

        // --- Monta DTOs esperados ---
        DadosTransferenciaUsuarioDTO pagador = new DadosTransferenciaUsuarioDTO();
        pagador.setNome("João da Silva");
        pagador.setCpfCnpj("12345678900");
        pagador.setAgencia("0001");
        pagador.setNumeroConta("111111-1");

        DadosTransferenciaUsuarioDTO recebedor = new DadosTransferenciaUsuarioDTO();
        recebedor.setNome("Maria Souza");
        recebedor.setCpfCnpj("98765432100");
        recebedor.setAgencia("0001");
        recebedor.setNumeroConta("222222-2");

        DadosHistoricoTransferenciaDTO historico = new DadosHistoricoTransferenciaDTO();
        historico.setDataTransferencia(ZonedDateTime.now());
        historico.setValorTransferencia(BigDecimal.valueOf(500.00));
        historico.setStatus(br.com.dourado.pagamento.simplificado.api.domain.enums.StatusTransferenciaEnum.EFETUADA.EFETUADA);
        historico.setDescricao("Pagamento de serviço");

        extratoTransferencia = new ExtratoTransferenciaDTO();
        extratoTransferencia.setIdTransacao(1L);
        extratoTransferencia.setDadosPagador(pagador);
        extratoTransferencia.setDadosRecebedor(recebedor);
        extratoTransferencia.setDadosHistorico(historico);
    }

    @Test
    void deveRealizarTransferenciaComSucesso() {
        // given
        given(transferenciaService.realizarTransacao(transferenciaRequest)).willReturn(extratoTransferencia);

        // when
        ResponseEntity<ExtratoTransferenciaDTO> response = transferenciaController.fazerTransferencia(transferenciaRequest);

        // then
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());

        ExtratoTransferenciaDTO body = response.getBody();
        assertEquals(1L, body.getIdTransacao());
        assertEquals("João da Silva", body.getDadosPagador().getNome());
        assertEquals("12345678900", body.getDadosPagador().getCpfCnpj());
        assertEquals("Maria Souza", body.getDadosRecebedor().getNome());
        assertEquals("98765432100", body.getDadosRecebedor().getCpfCnpj());
        assertEquals(BigDecimal.valueOf(500.00), body.getDadosHistorico().getValorTransferencia());
        assertEquals(br.com.dourado.pagamento.simplificado.api.domain.enums.StatusTransferenciaEnum.EFETUADA, body.getDadosHistorico().getStatus());
        assertEquals("Pagamento de serviço", body.getDadosHistorico().getDescricao());

        then(transferenciaService).should().realizarTransacao(transferenciaRequest);
    }

    @Test
    void deveChamarServicoComRequestCorreto() {
        // given
        given(transferenciaService.realizarTransacao(any(TransferenciaRequestDTO.class))).willReturn(extratoTransferencia);

        // when
        transferenciaController.fazerTransferencia(transferenciaRequest);

        // then
        then(transferenciaService).should(times(1)).realizarTransacao(transferenciaRequest);
    }
}