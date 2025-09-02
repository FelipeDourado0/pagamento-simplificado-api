package br.com.dourado.pagamento.simplificado.api.controller;

import br.com.dourado.pagamento.simplificado.api.domain.dtos.autenticacao.AutenticacaoRequestDTO;
import br.com.dourado.pagamento.simplificado.api.domain.dtos.autenticacao.LoginResponseDTO;
import br.com.dourado.pagamento.simplificado.api.domain.dtos.autenticacao.RegistroRequestDTO;
import br.com.dourado.pagamento.simplificado.api.domain.entities.Usuario;
import br.com.dourado.pagamento.simplificado.api.infra.exceptions.BasicAuthenticationEntryPointExeption;
import br.com.dourado.pagamento.simplificado.api.service.TokenService;
import br.com.dourado.pagamento.simplificado.api.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AutenticacaoController {
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private AuthenticationManager authenticationManager;


    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AutenticacaoRequestDTO data) {
        try {

            UsernamePasswordAuthenticationToken userNamePassowrd = new UsernamePasswordAuthenticationToken(data.getEmail(), data.getSenha());
            Usuario usuario = (Usuario) this.authenticationManager.authenticate(userNamePassowrd).getPrincipal();

            String token = tokenService.generateToken(usuario);

            return ResponseEntity.ok(new LoginResponseDTO(token));
        } catch (RuntimeException e) {
            throw new BasicAuthenticationEntryPointExeption("Erro ao fazer login.", "Credenciais inválidas.");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody @Valid RegistroRequestDTO data) {
        usuarioService.cadastrarUsuario(data);

        return ResponseEntity.ok().build();
    }
}
