package br.com.dourado.pagamento.simplificado.api.service;

import br.com.dourado.pagamento.simplificado.api.domain.entities.Conta;
import br.com.dourado.pagamento.simplificado.api.domain.entities.Usuario;
import br.com.dourado.pagamento.simplificado.api.domain.repositories.ContaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ContaServiceTest {

    @Mock
    private ContaRepository contaRepository;

    @InjectMocks
    private ContaService contaService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("João da Silva");
        usuario.setCpfCnpj("12345678900");
    }

    @Test
    void deveCadastrarContaCorrenteComSucesso() {
        // given
        BigDecimal saldoInicial = BigDecimal.valueOf(1500.00);

        // when
        contaService.cadastrarContaCorrente(usuario, saldoInicial);

        // then
        ArgumentCaptor<Conta> captor = ArgumentCaptor.forClass(Conta.class);
        verify(contaRepository, times(1)).save(captor.capture());
        verify(contaRepository, times(1)).flush();

        Conta contaSalva = captor.getValue();
        assertNotNull(contaSalva.getNumeroConta());
        assertTrue(contaSalva.getNumeroConta().matches("\\d{8}-\\d"),
                "Conta deve ter formato ########-# (8 dígitos + hífen + dígito)");
        assertNotNull(contaSalva.getAgencia());
        assertTrue(contaSalva.getAgencia().matches("\\d{4}"),
                "Agência deve ter 4 dígitos");
        assertEquals(saldoInicial, contaSalva.getSaldo());
        assertEquals(usuario, contaSalva.getUsuario());
    }

    @Test
    void deveGerarNumerosDeAgenciaValidos() {
        // executa várias vezes para garantir faixa correta
        for (int i = 0; i < 20; i++) {
            String agencia = invokeGerarAgencia();
            assertTrue(agencia.matches("\\d{4}"));
            int valor = Integer.parseInt(agencia);
            assertTrue(valor >= 1000 && valor <= 9999);
        }
    }

    @Test
    void deveGerarNumerosDeContaCorrenteValidos() {
        // executa várias vezes para garantir formato
        for (int i = 0; i < 20; i++) {
            String numeroConta = invokeGerarContaCorrente();
            assertTrue(numeroConta.matches("\\d{8}-\\d"),
                    "Conta deve estar no formato ########-#");
        }
    }

    // -------- Helpers para acessar métodos privados via reflexão --------
    private String invokeGerarAgencia() {
        try {
            var method = ContaService.class.getDeclaredMethod("gerarAgencia");
            method.setAccessible(true);
            return (String) method.invoke(contaService);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String invokeGerarContaCorrente() {
        try {
            var method = ContaService.class.getDeclaredMethod("gerarContaCorrente");
            method.setAccessible(true);
            return (String) method.invoke(contaService);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}