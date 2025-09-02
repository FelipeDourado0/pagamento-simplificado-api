package br.com.dourado.pagamento.simplificado.api.controller;

import br.com.dourado.pagamento.simplificado.api.domain.dtos.autenticacao.AutenticacaoRequestDTO;
import br.com.dourado.pagamento.simplificado.api.domain.dtos.autenticacao.LoginResponseDTO;
import br.com.dourado.pagamento.simplificado.api.domain.dtos.autenticacao.RegistroRequestDTO;
import br.com.dourado.pagamento.simplificado.api.domain.entities.Usuario;
import br.com.dourado.pagamento.simplificado.api.infra.exceptions.BasicAuthenticationEntryPointExeption;
import br.com.dourado.pagamento.simplificado.api.service.TokenService;
import br.com.dourado.pagamento.simplificado.api.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.mock;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

@ExtendWith(MockitoExtension.class)
class AutenticacaoControllerTest {

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private TokenService tokenService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AutenticacaoController autenticacaoController;

    private AutenticacaoRequestDTO loginRequest;
    private RegistroRequestDTO registroRequest;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        loginRequest = new AutenticacaoRequestDTO("teste@teste.com", "123456");

        registroRequest = new RegistroRequestDTO();
        registroRequest.setNome("Teste da Silva");
        registroRequest.setEmail("teste@teste.com");
        registroRequest.setSenha("123456");
        registroRequest.setCpfCnpj("12345678900");
        registroRequest.setIsPessoaJuridica(false);
        registroRequest.setPerfil("CLIENTE");
        registroRequest.setSaldoInicial(BigDecimal.valueOf(1000.00));

        usuario = new Usuario();
        usuario.setEmail("teste@teste.com");
        usuario.setSenha("123456");
    }

    @Test
    void deveFazerLoginComSucesso() {
        // given
        Authentication authMock = mock(Authentication.class);
        given(authMock.getPrincipal()).willReturn(usuario);
        given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .willReturn(authMock);
        given(tokenService.generateToken(usuario)).willReturn("fake-jwt-token");

        // when
        ResponseEntity<LoginResponseDTO> response = autenticacaoController.login(loginRequest);

        // then
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("fake-jwt-token", response.getBody().token());
        then(authenticationManager).should().authenticate(any(UsernamePasswordAuthenticationToken.class));
        then(tokenService).should().generateToken(usuario);
    }

    @Test
    void deveLancarExcecaoQuandoLoginFalhar() {
        // given
        given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .willThrow(new RuntimeException("Credenciais inválidas"));

        // when / then
        BasicAuthenticationEntryPointExeption exception = assertThrows(
                BasicAuthenticationEntryPointExeption.class,
                () -> autenticacaoController.login(loginRequest)
        );

        assertEquals("Erro ao fazer login.", exception.getTitulo());
        assertEquals("Credenciais inválidas.", exception.getMenssagem());
    }

    @Test
    void deveRegistrarUsuarioComSucesso() {
        // given
        willDoNothing().given(usuarioService).cadastrarUsuario(registroRequest);

        // when
        ResponseEntity<Object> response = autenticacaoController.register(registroRequest);

        // then
        assertEquals(200, response.getStatusCodeValue());
        assertFalse(response.hasBody());
        then(usuarioService).should().cadastrarUsuario(registroRequest);
    }
}