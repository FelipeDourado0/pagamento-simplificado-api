package br.com.dourado.pagamento.simplificado.api.controller.interfce;

import br.com.dourado.pagamento.simplificado.api.domain.dtos.transferencia.ExtratoTransferenciaDTO;
import br.com.dourado.pagamento.simplificado.api.domain.dtos.transferencia.TransferenciaRequestDTO;
import br.com.dourado.pagamento.simplificado.api.infra.exceptions.ResponseExceptionDefault;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Transferência", description = "Operações de transferência de valores entre contas")
@RequestMapping("/transferencia")
public interface ITransferenciaController {

    @Operation(
            summary = "Realizar uma transferência",
            description = "Permite que usuários com perfil CLIENTE realizem transferências para outras contas."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Transferência realizada com sucesso",
            content = @Content(schema = @Schema(implementation = ExtratoTransferenciaDTO.class))
    )
    @ApiResponse(responseCode = "400", description = "Dados inválidos ou saldo insuficiente", content = @Content(schema = @Schema(implementation = ResponseExceptionDefault.class)))
    @ApiResponse(responseCode = "403", description = "Acesso negado — somente usuários com o papel CLIENTE podem realizar transferências", content = @Content(schema = @Schema(implementation = ResponseExceptionDefault.class)))
    @PostMapping
    @PreAuthorize("hasRole('CLIENTE')")
    ResponseEntity<ExtratoTransferenciaDTO> fazerTransferencia(
            @RequestBody @Valid TransferenciaRequestDTO data
    );
}
