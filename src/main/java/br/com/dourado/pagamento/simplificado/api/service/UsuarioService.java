package br.com.dourado.pagamento.simplificado.api.service;

import br.com.dourado.pagamento.simplificado.api.domain.dtos.autenticacao.RegistroRequestDTO;
import br.com.dourado.pagamento.simplificado.api.domain.dtos.conta.ContaDTO;
import br.com.dourado.pagamento.simplificado.api.domain.dtos.conta.DadosContaResponseDTO;
import br.com.dourado.pagamento.simplificado.api.domain.dtos.conta.UsuarioDTO;
import br.com.dourado.pagamento.simplificado.api.domain.entities.Conta;
import br.com.dourado.pagamento.simplificado.api.domain.entities.HistoricoTransacao;
import br.com.dourado.pagamento.simplificado.api.domain.entities.Perfil;
import br.com.dourado.pagamento.simplificado.api.domain.entities.Usuario;
import br.com.dourado.pagamento.simplificado.api.domain.repositories.ContaRepository;
import br.com.dourado.pagamento.simplificado.api.domain.repositories.HistoricoTransacaoRepository;
import br.com.dourado.pagamento.simplificado.api.domain.repositories.PerfilRepository;
import br.com.dourado.pagamento.simplificado.api.domain.repositories.UsuarioRepository;
import br.com.dourado.pagamento.simplificado.api.infra.exceptions.BadRequestExeption;
import br.com.dourado.pagamento.simplificado.api.infra.exceptions.NotFoundExeption;
import br.com.dourado.pagamento.simplificado.api.infra.helper.LoggerHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PerfilRepository perfilRepository;
    @Autowired
    private ContaService contaService;
    @Autowired
    private ContaRepository contaRepository;
    @Autowired
    private HistoricoTransacaoRepository historicoTransacaoRepository;
    @Autowired
    private LoggerHelper loggerHelper;

    public void cadastrarUsuario(RegistroRequestDTO dataRegistro) {
        loggerHelper.info(this.getClass(), "Criação de usuário.");

        verificaSeUsuarioCadastrado(dataRegistro.getEmail(), dataRegistro.getCpfCnpj());
        Usuario usuarioCadastrado = criarUsuario(dataRegistro);
        criarContaCorrente(usuarioCadastrado, dataRegistro.getSaldoInicial());
        loggerHelper.info(this.getClass(), "Usuário cadastrado com sucesso.");
    }

    public DadosContaResponseDTO obterDadosConta(String cpfCnpj) {
        loggerHelper.info(this.getClass(), "Iniciou busca de conta do usuário - " + ZonedDateTime.now());
        Conta conta = contaRepository.findByUsuarioCpfCnpj(cpfCnpj)
                .orElseThrow(() -> new NotFoundExeption("Usuario não encontrado.", "Usuario não encontrado no banco."));
        Usuario usuario = conta.getUsuario();
        UsuarioDTO usuarioDTO = new UsuarioDTO().fromUsuarioEntity(usuario);

        List<HistoricoTransacao> listaHistoricoTransacao = historicoTransacaoRepository.buscarHistoricoPorCpf(usuario.getCpfCnpj());
        ContaDTO contaDTO = new ContaDTO().fromContaEntity(conta, listaHistoricoTransacao);

        loggerHelper.info(this.getClass(), "Finalizou busca de conta do usuário - " + ZonedDateTime.now());
        return new DadosContaResponseDTO(usuarioDTO, contaDTO);
    }

    private void verificaSeUsuarioCadastrado(String email, String cpfCnpj) {
        boolean usuarioCadastrado = usuarioRepository.existsByEmailOrCpfCnpj(email, cpfCnpj);
        if (usuarioCadastrado)
            throw new BadRequestExeption("Usuário não cadastrado.", "Email ou cpf/cnpj ja existe.");
    }

    private Usuario criarUsuario(RegistroRequestDTO dataRegistro) {
        Perfil perfil = perfilRepository.findByNome(dataRegistro.getPerfil())
                .orElseThrow(() -> new BadRequestExeption("Usuário não cadastrado.", "Perfil não permitido."));

        String ecryptedPassword = new BCryptPasswordEncoder().encode(dataRegistro.getSenha());

        Usuario novoUsuario;
        novoUsuario = new Usuario()
                .setNome(dataRegistro.getNome())
                .setSenha(ecryptedPassword)
                .setCpfCnpj(dataRegistro.getCpfCnpj())
                .setEmail(dataRegistro.getEmail())
                .setPessoaJuridica(dataRegistro.getIsPessoaJuridica())
                .setAtivo(true)
                .setPerfil(perfil);

        return salvarUsuario(novoUsuario);
    }

    private void criarContaCorrente(Usuario usuario, BigDecimal saldo) {
        contaService.cadastrarContaCorrente(usuario, saldo);
    }

    private Usuario salvarUsuario(Usuario novoUsuario) {
        Usuario usuarioSalvo = usuarioRepository.save(novoUsuario);
        usuarioRepository.flush();
        return usuarioSalvo;
    }
}
