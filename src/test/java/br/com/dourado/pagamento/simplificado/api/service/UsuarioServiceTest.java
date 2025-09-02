package br.com.dourado.pagamento.simplificado.api.service;

import br.com.dourado.pagamento.simplificado.api.domain.dtos.autenticacao.RegistroRequestDTO;
import br.com.dourado.pagamento.simplificado.api.domain.dtos.conta.DadosContaResponseDTO;
import br.com.dourado.pagamento.simplificado.api.domain.entities.Conta;
import br.com.dourado.pagamento.simplificado.api.domain.entities.Perfil;
import br.com.dourado.pagamento.simplificado.api.domain.entities.Usuario;
import br.com.dourado.pagamento.simplificado.api.domain.repositories.ContaRepository;
import br.com.dourado.pagamento.simplificado.api.domain.repositories.HistoricoTransacaoRepository;
import br.com.dourado.pagamento.simplificado.api.domain.repositories.PerfilRepository;
import br.com.dourado.pagamento.simplificado.api.domain.repositories.UsuarioRepository;
import br.com.dourado.pagamento.simplificado.api.infra.exceptions.BadRequestExeption;
import br.com.dourado.pagamento.simplificado.api.infra.exceptions.NotFoundExeption;
import br.com.dourado.pagamento.simplificado.api.infra.helper.LoggerHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @InjectMocks
    private UsuarioService usuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private PerfilRepository perfilRepository;
    @Mock
    private ContaService contaService;
    @Mock
    private ContaRepository contaRepository;
    @Mock
    private HistoricoTransacaoRepository historicoTransacaoRepository;
    @Mock
    private LoggerHelper loggerHelper;

    private RegistroRequestDTO registroRequest;
    private Perfil perfilCliente;
    private Usuario usuario;
    private Conta conta;

    @BeforeEach
    void setUp() {
        registroRequest = new RegistroRequestDTO();
        registroRequest.setNome("João da Silva");
        registroRequest.setEmail("joao@teste.com");
        registroRequest.setCpfCnpj("12345678900");
        registroRequest.setSenha("senha123");
        registroRequest.setIsPessoaJuridica(false);
        registroRequest.setPerfil("CLIENTE");
        registroRequest.setSaldoInicial(BigDecimal.valueOf(1000));

        perfilCliente = new Perfil();
        perfilCliente.setId(1L);
        perfilCliente.setNome("CLIENTE");

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("João da Silva");
        usuario.setEmail("joao@teste.com");
        usuario.setCpfCnpj("12345678900");

        conta = new Conta();
        conta.setId(1L);
        conta.setUsuario(usuario);
        conta.setAgencia("0001");
        conta.setNumeroConta("123456-7");
        conta.setSaldo(BigDecimal.valueOf(1000));
    }

    @Test
    void deveCadastrarUsuarioComSucesso() {
        // when
        when(usuarioRepository.existsByEmailOrCpfCnpj("joao@teste.com", "12345678900"))
                .thenReturn(false);
        when(perfilRepository.findByNome("CLIENTE"))
                .thenReturn(Optional.of(perfilCliente));
        when(usuarioRepository.save(any(Usuario.class)))
                .thenReturn(usuario);

        // action
        usuarioService.cadastrarUsuario(registroRequest);

        // then
        ArgumentCaptor<Usuario> usuarioCaptor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).save(usuarioCaptor.capture());
        verify(usuarioRepository).flush();
        verify(contaService).cadastrarContaCorrente(any(Usuario.class), eq(BigDecimal.valueOf(1000)));

        Usuario usuarioSalvo = usuarioCaptor.getValue();
        assertEquals("João da Silva", usuarioSalvo.getNome());
        assertEquals("joao@teste.com", usuarioSalvo.getEmail());
        assertEquals("12345678900", usuarioSalvo.getCpfCnpj());
        assertFalse(usuarioSalvo.isPessoaJuridica());
        assertTrue(usuarioSalvo.isAtivo());
        assertEquals(perfilCliente, usuarioSalvo.getPerfil());
        assertNotEquals("senha123", usuarioSalvo.getSenha()); // senha deve estar criptografada
    }

    @Test
    void naoDeveCadastrarUsuarioJaExistente() {
        // when
        when(usuarioRepository.existsByEmailOrCpfCnpj("joao@teste.com", "12345678900"))
                .thenReturn(true);

        // then
        BadRequestExeption exception = assertThrows(
                BadRequestExeption.class,
                () -> usuarioService.cadastrarUsuario(registroRequest)
        );

        assertEquals("Usuário não cadastrado.", exception.getTitulo());
        assertEquals("Email ou cpf/cnpj ja existe.", exception.getMenssagem());

        verify(usuarioRepository, never()).save(any());
        verify(contaService, never()).cadastrarContaCorrente(any(), any());
    }

    @Test
    void naoDeveCadastrarUsuarioComPerfilInexistente() {
        // when
        when(usuarioRepository.existsByEmailOrCpfCnpj("joao@teste.com", "12345678900"))
                .thenReturn(false);
        when(perfilRepository.findByNome("PERFIL_INEXISTENTE"))
                .thenReturn(Optional.empty());

        registroRequest.setPerfil("PERFIL_INEXISTENTE");

        // then
        BadRequestExeption exception = assertThrows(
                BadRequestExeption.class,
                () -> usuarioService.cadastrarUsuario(registroRequest)
        );

        assertEquals("Usuário não cadastrado.", exception.getTitulo());
        assertEquals("Perfil não permitido.", exception.getMenssagem());
    }

    @Test
    void deveObterDadosContaComSucesso() {
        // when
        when(contaRepository.findByUsuarioCpfCnpj("12345678900"))
                .thenReturn(Optional.of(conta));
        when(historicoTransacaoRepository.buscarHistoricoPorCpf("12345678900"))
                .thenReturn(List.of());

        // action
        DadosContaResponseDTO resultado = usuarioService.obterDadosConta("12345678900");

        // then
        assertNotNull(resultado);
        assertNotNull(resultado.getDadosUsuario());
        assertNotNull(resultado.getDadosConta());

        assertEquals("João da Silva", resultado.getDadosUsuario().getNome());
        assertEquals("joao@teste.com", resultado.getDadosUsuario().getEmail());
        assertEquals("12345678900", resultado.getDadosUsuario().getCpfCnpj());

        assertEquals("0001", resultado.getDadosConta().getAgencia());
        assertEquals("123456-7", resultado.getDadosConta().getNumeroConta());
        assertEquals(BigDecimal.valueOf(1000), resultado.getDadosConta().getSaldo());

        verify(contaRepository).findByUsuarioCpfCnpj("12345678900");
        verify(historicoTransacaoRepository).buscarHistoricoPorCpf("12345678900");
    }

    @Test
    void deveLancarNotFoundQuandoContaNaoExistir() {
        // when
        when(contaRepository.findByUsuarioCpfCnpj("99999999999"))
                .thenReturn(Optional.empty());

        // then
        NotFoundExeption exception = assertThrows(
                NotFoundExeption.class,
                () -> usuarioService.obterDadosConta("99999999999")
        );

        assertEquals("Usuario não encontrado.", exception.getTitulo());
        assertEquals("Usuario não encontrado no banco.", exception.getMenssagem());

        verify(contaRepository).findByUsuarioCpfCnpj("99999999999");
        verify(historicoTransacaoRepository, never()).buscarHistoricoPorCpf(any());
    }
}