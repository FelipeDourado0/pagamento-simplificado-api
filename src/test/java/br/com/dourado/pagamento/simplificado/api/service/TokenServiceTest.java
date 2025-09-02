package br.com.dourado.pagamento.simplificado.api.service;

import br.com.dourado.pagamento.simplificado.api.domain.entities.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @InjectMocks
    private TokenService tokenService;

    private Usuario usuario;

    @BeforeEach
    void setUp() throws Exception {
        // forçando o valor da secret (injeção manual via reflexão)
        Field secretField = TokenService.class.getDeclaredField("secret");
        secretField.setAccessible(true);
        secretField.set(tokenService, "secret123");

        usuario = new Usuario();
        usuario.setEmail("joao@teste.com");
    }

    @Test
    void deveGerarTokenComSucesso() {
        // when
        String token = tokenService.generateToken(usuario);

        // then
        assertNotNull(token);
        assertFalse(token.isEmpty());

        // valida que o mesmo token pode ser checado
        String subject = tokenService.validateToken(token);
        assertEquals("joao@teste.com", subject);
    }

    @Test
    void deveValidarTokenEDevolverEmail() {
        String token = tokenService.generateToken(usuario);

        String subject = tokenService.validateToken(token);

        assertEquals("joao@teste.com", subject);
    }
}