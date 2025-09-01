package br.com.dourado.pagamento.simplificado.api.controller;

import br.com.dourado.pagamento.simplificado.api.domain.dtos.AthenticationRequestDTO;
import br.com.dourado.pagamento.simplificado.api.domain.dtos.LoginResponseDTO;
import br.com.dourado.pagamento.simplificado.api.domain.dtos.RegistroRequestDTO;
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
public class AuthenticationController {
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private AuthenticationManager authenticationManager;


    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AthenticationRequestDTO data) {
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
        usuarioService.criarUsuario(data);

        return ResponseEntity.ok().build();
    }
}
