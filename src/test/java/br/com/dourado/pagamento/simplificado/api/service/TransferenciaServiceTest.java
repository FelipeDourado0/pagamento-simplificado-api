package br.com.dourado.pagamento.simplificado.api.service;

import br.com.dourado.pagamento.simplificado.api.domain.dtos.transferencia.ExtratoTransferenciaDTO;
import br.com.dourado.pagamento.simplificado.api.domain.dtos.transferencia.TransferenciaRequestDTO;
import br.com.dourado.pagamento.simplificado.api.domain.entities.Conta;
import br.com.dourado.pagamento.simplificado.api.domain.entities.HistoricoTransacao;
import br.com.dourado.pagamento.simplificado.api.domain.entities.Usuario;
import br.com.dourado.pagamento.simplificado.api.domain.repositories.ContaRepository;
import br.com.dourado.pagamento.simplificado.api.domain.repositories.UsuarioRepository;
import br.com.dourado.pagamento.simplificado.api.infra.exceptions.AccessDeniedException;
import br.com.dourado.pagamento.simplificado.api.infra.exceptions.BadRequestExeption;
import br.com.dourado.pagamento.simplificado.api.infra.exceptions.NotFoundExeption;
import br.com.dourado.pagamento.simplificado.api.infra.helper.LoggerHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransferenciaServiceTest {

    @InjectMocks
    private TransferenciaService transferenciaService;

    @Mock
    private LoggerHelper loggerHelper;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private ContaRepository contaRepository;
    @Mock
    private HistoricoTransacaoService historicoTransacaoService;

    private Conta contaOrigem;
    private Conta contaDestino;
    private Usuario usuarioOrigem;
    private Usuario usuarioDestino;

    @BeforeEach
    void setUp() {
        usuarioOrigem = new Usuario();
        usuarioOrigem.setId(1L);
        usuarioOrigem.setEmail("origem@test.com");
        usuarioOrigem.setPessoaJuridica(false);

        usuarioDestino = new Usuario();
        usuarioDestino.setId(2L);
        usuarioDestino.setEmail("destino@test.com");
        usuarioDestino.setPessoaJuridica(false);

        contaOrigem = new Conta();
        contaOrigem.setId(1L);
        contaOrigem.setUsuario(usuarioOrigem);
        contaOrigem.setSaldo(BigDecimal.valueOf(1000));

        contaDestino = new Conta();
        contaDestino.setId(2L);
        contaDestino.setUsuario(usuarioDestino);
        contaDestino.setSaldo(BigDecimal.valueOf(500));
    }

    @Test
    void deveRealizarTransferenciaComSucesso() {
        TransferenciaRequestDTO req = new TransferenciaRequestDTO();
        req.setEmailOrigem("origem@test.com");
        req.setEmailDestino("destino@test.com");
        req.setValorTransferencia(BigDecimal.valueOf(200));
        req.setDescricao("Pagamento");

        when(contaRepository.findByUsuarioEmail("origem@test.com")).thenReturn(Optional.of(contaOrigem));
        when(contaRepository.findByUsuarioEmail("destino@test.com")).thenReturn(Optional.of(contaDestino));
        when(historicoTransacaoService.criarHistoricoTransacao(any(), any(), any(), any()))
                .thenReturn(new HistoricoTransacao().setTransacaoConcluida(true));

        ExtratoTransferenciaDTO resultado = transferenciaService.realizarTransacao(req);

        assertNotNull(resultado);
        assertEquals(BigDecimal.valueOf(800), contaOrigem.getSaldo());
        assertEquals(BigDecimal.valueOf(700), contaDestino.getSaldo());
    }

    @Test
    void naoDevePermitirTransferirParaMesmaConta() {
        TransferenciaRequestDTO req = new TransferenciaRequestDTO();
        req.setEmailOrigem("igual@test.com");
        req.setEmailDestino("igual@test.com");
        req.setValorTransferencia(BigDecimal.valueOf(100));

        assertThrows(IllegalArgumentException.class, () -> transferenciaService.realizarTransacao(req));
    }

    @Test
    void naoDevePermitirValorZeroOuNegativo() {
        TransferenciaRequestDTO req = new TransferenciaRequestDTO();
        req.setEmailOrigem("origem@test.com");
        req.setEmailDestino("destino@test.com");
        req.setValorTransferencia(BigDecimal.ZERO);

        assertThrows(IllegalArgumentException.class, () -> transferenciaService.realizarTransacao(req));
    }

    @Test
    void naoDevePermitirSaldoCircular() {
        usuarioDestino.setId(1L); // mesmo usuário
        contaDestino.setUsuario(usuarioOrigem);

        TransferenciaRequestDTO req = new TransferenciaRequestDTO();
        req.setEmailOrigem("origem@test.com");
        req.setEmailDestino("destino@test.com");
        req.setValorTransferencia(BigDecimal.valueOf(100));

        when(contaRepository.findByUsuarioEmail("origem@test.com")).thenReturn(Optional.of(contaOrigem));
        when(contaRepository.findByUsuarioEmail("destino@test.com")).thenReturn(Optional.of(contaDestino));

        assertThrows(BadRequestExeption.class, () -> transferenciaService.realizarTransacao(req));
    }

    @Test
    void naoDevePermitirSaldoInsuficiente() {
        contaOrigem.setSaldo(BigDecimal.valueOf(50));
        TransferenciaRequestDTO req = new TransferenciaRequestDTO();
        req.setEmailOrigem("origem@test.com");
        req.setEmailDestino("destino@test.com");
        req.setValorTransferencia(BigDecimal.valueOf(200));

        when(contaRepository.findByUsuarioEmail("origem@test.com")).thenReturn(Optional.of(contaOrigem));
        when(contaRepository.findByUsuarioEmail("destino@test.com")).thenReturn(Optional.of(contaDestino));

        assertThrows(BadRequestExeption.class, () -> transferenciaService.realizarTransacao(req));
    }

    @Test
    void naoDevePermitirPessoaJuridicaTransferindo() {
        usuarioOrigem.setPessoaJuridica(true);
        TransferenciaRequestDTO req = new TransferenciaRequestDTO();
        req.setEmailOrigem("origem@test.com");
        req.setEmailDestino("destino@test.com");
        req.setValorTransferencia(BigDecimal.valueOf(100));

        when(contaRepository.findByUsuarioEmail("origem@test.com")).thenReturn(Optional.of(contaOrigem));
        when(contaRepository.findByUsuarioEmail("destino@test.com")).thenReturn(Optional.of(contaDestino));

        assertThrows(AccessDeniedException.class, () -> transferenciaService.realizarTransacao(req));
    }

    @Test
    void deveLancarNotFoundQuandoContaOrigemNaoExistir() {
        TransferenciaRequestDTO req = new TransferenciaRequestDTO();
        req.setEmailOrigem("naoexiste@test.com");
        req.setEmailDestino("destino@test.com");
        req.setValorTransferencia(BigDecimal.valueOf(100));

        when(contaRepository.findByUsuarioEmail("naoexiste@test.com")).thenReturn(Optional.empty());

        assertThrows(NotFoundExeption.class, () -> transferenciaService.realizarTransacao(req));
    }

    @Test
    void deveLancarNotFoundQuandoContaDestinoNaoExistir() {
        TransferenciaRequestDTO req = new TransferenciaRequestDTO();
        req.setEmailOrigem("origem@test.com");
        req.setEmailDestino("naoexiste@test.com");
        req.setValorTransferencia(BigDecimal.valueOf(100));

        when(contaRepository.findByUsuarioEmail("origem@test.com")).thenReturn(Optional.of(contaOrigem));
        when(contaRepository.findByUsuarioEmail("naoexiste@test.com")).thenReturn(Optional.empty());

        assertThrows(NotFoundExeption.class, () -> transferenciaService.realizarTransacao(req));
    }
}