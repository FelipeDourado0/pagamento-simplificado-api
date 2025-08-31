package br.com.dourado.pagamento.simplificado.api.service;

import br.com.dourado.pagamento.simplificado.api.domain.dtos.RegistroRequestDTO;
import br.com.dourado.pagamento.simplificado.api.domain.entities.Perfil;
import br.com.dourado.pagamento.simplificado.api.domain.entities.Usuario;
import br.com.dourado.pagamento.simplificado.api.domain.repositories.PerfilRepository;
import br.com.dourado.pagamento.simplificado.api.domain.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PerfilRepository perfilRepository;
    @Autowired
    private AuthenticationManager authenticationManager;

    public void criarUsuario(RegistroRequestDTO dataRegistro) {
        boolean usuarioCadastrado = usuarioRepository.existsByEmailOrCpfCnpj(dataRegistro.getEmail(), dataRegistro.getCpfCnpj());
        if (usuarioCadastrado)
            throw new RuntimeException("Email ou cpf/cnpj ja existe.");

        Perfil perfil = perfilRepository.findByNome(dataRegistro.getPerfil())
                .orElseThrow(() -> new RuntimeException("Perfil não permitido."));

        String ecryptedPassword = new BCryptPasswordEncoder().encode(dataRegistro.getSenha());
        Usuario novoUsuario = new Usuario()
                .setNome(dataRegistro.getNome())
                .setSenha(ecryptedPassword)
                .setCpfCnpj(dataRegistro.getCpfCnpj())
                .setEmail(dataRegistro.getEmail())
                .setPessoaJuridica(dataRegistro.getIsPessoaJuridica())
                .setAtivo(true)
                .setPerfil(perfil);

        usuarioRepository.save(novoUsuario);
        usuarioRepository.flush();
    }
}
