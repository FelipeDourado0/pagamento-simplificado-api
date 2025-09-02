package br.com.dourado.pagamento.simplificado.api.service;

import br.com.dourado.pagamento.simplificado.api.domain.entities.Usuario;
import br.com.dourado.pagamento.simplificado.api.domain.repositories.UsuarioRepository;
import br.com.dourado.pagamento.simplificado.api.infra.exceptions.NotFoundExeption;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthorizationServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private AuthorizationService authorizationService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("João da Silva");
        usuario.setEmail("joao@teste.com");
        usuario.setCpfCnpj("12345678900");
        usuario.setSenha("senhaCriptografada"); // precisa implementar UserDetails
        usuario.setAtivo(true);
    }

    @Test
    void deveRetornarUsuarioQuandoEncontradoPorEmail() {
        // when (Mockito style)
        when(usuarioRepository.findByEmail("joao@teste.com"))
                .thenReturn(Optional.of(usuario));

        // action
        UserDetails userDetails = authorizationService.loadUserByUsername("joao@teste.com");

        // then
        assertNotNull(userDetails);
        assertEquals("joao@teste.com", userDetails.getUsername());
        assertEquals("senhaCriptografada", userDetails.getPassword());
        assertTrue(userDetails.isEnabled());

        verify(usuarioRepository).findByEmail("joao@teste.com");
    }

    @Test
    void deveLancarNotFoundExeptionQuandoUsuarioNaoForEncontrado() {
        // when
        when(usuarioRepository.findByEmail("naoexiste@teste.com"))
                .thenReturn(Optional.empty());

        // then
        NotFoundExeption exception = assertThrows(
                NotFoundExeption.class,
                () -> authorizationService.loadUserByUsername("naoexiste@teste.com")
        );

        assertEquals("Usuario não encontrado.", exception.getTitulo());
        assertEquals("Email não corresponde na base de dados.", exception.getMenssagem());

        verify(usuarioRepository).findByEmail("naoexiste@teste.com");
    }
}