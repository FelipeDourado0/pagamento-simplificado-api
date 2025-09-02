package br.com.dourado.pagamento.simplificado.api.controller.interfce;

import br.com.dourado.pagamento.simplificado.api.domain.dtos.autenticacao.AutenticacaoRequestDTO;
import br.com.dourado.pagamento.simplificado.api.domain.dtos.autenticacao.LoginResponseDTO;
import br.com.dourado.pagamento.simplificado.api.domain.dtos.autenticacao.RegistroRequestDTO;
import br.com.dourado.pagamento.simplificado.api.infra.exceptions.ResponseExceptionDefault;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Autenticação", description = "Endpoints para login e registro de usuários")
@RequestMapping("/auth")
public interface IAutenticacaoController {

    @Operation(
            summary = "Realizar login do usuário",
            description = "Autentica um usuário com e-mail e senha, retornando um token JWT"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Login efetuado com sucesso",
            content = @Content(schema = @Schema(implementation = LoginResponseDTO.class))
    )
    @ApiResponse(responseCode = "401", description = "Credenciais inválidas", content = @Content(schema = @Schema(implementation = ResponseExceptionDefault.class)))
    @PostMapping("/login")
    ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AutenticacaoRequestDTO data);

    @Operation(
            summary = "Registrar novo usuário",
            description = "Registra um novo usuário no sistema"
    )
    @ApiResponse(responseCode = "200", description = "Usuário registrado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos", content = @Content(schema = @Schema(implementation = ResponseExceptionDefault.class)))
    @PostMapping("/register")
    ResponseEntity<Object> register(@RequestBody @Valid RegistroRequestDTO data);
}
