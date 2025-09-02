package br.com.dourado.pagamento.simplificado.api.controller;

import br.com.dourado.pagamento.simplificado.api.domain.dtos.conta.ContaDTO;
import br.com.dourado.pagamento.simplificado.api.domain.dtos.conta.DadosContaResponseDTO;
import br.com.dourado.pagamento.simplificado.api.domain.dtos.conta.UsuarioDTO;
import br.com.dourado.pagamento.simplificado.api.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.anyString;
import static org.mockito.BDDMockito.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.never;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class ContaControllerTest {

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private ContaController contaController;

    private String cpfCnpjValido;
    private DadosContaResponseDTO dadosContaResponse;

    @BeforeEach
    void setUp() {
        cpfCnpjValido = "12345678900";

        // --- Criando DTOs simulados de retorno ---
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setNome("João da Silva");
        usuarioDTO.setEmail("joao@teste.com");
        usuarioDTO.setCpfCnpj(cpfCnpjValido);
        usuarioDTO.setPessoaJuridica(false);
        usuarioDTO.setAtivo(true);

        ContaDTO contaDTO = new ContaDTO();
        contaDTO.setAgencia("0001");
        contaDTO.setNumeroConta("123456-7");
        contaDTO.setSaldo(BigDecimal.valueOf(1500.00));
        contaDTO.setTransacoesEnviadas(List.of());
        contaDTO.setTransacoesRecebidas(List.of());

        dadosContaResponse = new DadosContaResponseDTO();
        dadosContaResponse.setDadosUsuario(usuarioDTO);
        dadosContaResponse.setDadosConta(contaDTO);
    }

    @Test
    void deveBuscarDadosContaComSucesso() {
        // given
        given(usuarioService.obterDadosConta(cpfCnpjValido)).willReturn(dadosContaResponse);

        // when
        ResponseEntity<DadosContaResponseDTO> response = contaController.buscarDadosPorEmail(cpfCnpjValido);

        // then
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());

        // valida dados do usuário
        assertEquals("João da Silva", response.getBody().getDadosUsuario().getNome());
        assertEquals("joao@teste.com", response.getBody().getDadosUsuario().getEmail());
        assertEquals(cpfCnpjValido, response.getBody().getDadosUsuario().getCpfCnpj());
        assertFalse(response.getBody().getDadosUsuario().isPessoaJuridica());
        assertTrue(response.getBody().getDadosUsuario().isAtivo());

        // valida dados da conta corrente
        assertEquals("0001", response.getBody().getDadosConta().getAgencia());
        assertEquals("123456-7", response.getBody().getDadosConta().getNumeroConta());
        assertEquals(BigDecimal.valueOf(1500.00), response.getBody().getDadosConta().getSaldo());

        // garante que service foi chamado corretamente
        then(usuarioService).should().obterDadosConta(cpfCnpjValido);
    }

    @Test
    void deveRetornarResponseEntityOkQuandoEncontrarConta() {
        // given
        given(usuarioService.obterDadosConta(anyString())).willReturn(dadosContaResponse);

        // when
        ResponseEntity<DadosContaResponseDTO> response = contaController.buscarDadosPorEmail("11111111111");

        // then
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        then(usuarioService).should().obterDadosConta("11111111111");
    }

    @Test
    void devePassarCpfCnpjCorretoParaService() {
        // given
        String cpfTeste = "98765432100";
        given(usuarioService.obterDadosConta(cpfTeste)).willReturn(dadosContaResponse);

        // when
        contaController.buscarDadosPorEmail(cpfTeste);

        // then
        then(usuarioService).should().obterDadosConta(cpfTeste);
        then(usuarioService).should(never()).obterDadosConta(argThat(cpf -> !cpf.equals(cpfTeste)));
    }
}