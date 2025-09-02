package br.com.dourado.pagamento.simplificado.api.controller.interfce;

import br.com.dourado.pagamento.simplificado.api.domain.dtos.conta.DadosContaResponseDTO;
import br.com.dourado.pagamento.simplificado.api.infra.exceptions.ResponseExceptionDefault;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Conta", description = "Endpoints de consulta de conta corrente")
@RequestMapping("/conta")
public interface IContaController {

    @Operation(
            summary = "Buscar conta por CPF/CNPJ",
            description = "Retorna os dados da conta corrente vinculada ao CPF/CNPJ informado."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Dados da conta retornados com sucesso",
            content = @Content(schema = @Schema(implementation = DadosContaResponseDTO.class))
    )
    @ApiResponse(responseCode = "403", description = "Acesso negado — usuário não possui perfil CLIENTE ou LOJISTA", content = @Content(schema = @Schema(implementation = ResponseExceptionDefault.class)))
    @ApiResponse(responseCode = "404", description = "Conta não encontrada para o CPF/CNPJ informado", content = @Content(schema = @Schema(implementation = ResponseExceptionDefault.class)))
    @GetMapping
    @PreAuthorize("hasAnyRole('CLIENTE', 'LOJISTA')")
    ResponseEntity<DadosContaResponseDTO> buscarDadosPorEmail(
            @Parameter(description = "CPF ou CNPJ do usuário", example = "123.456.789-01")
            @RequestParam String cpfCnpj
    );
}