package br.com.dourado.pagamento.simplificado.api.domain.dtos.transferencia;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferenciaRequestDTO {
    @NonNull
    @Email
    private String emailOrigem;
    @NonNull
    @Email
    private String emailDestino;
    @NonNull
    @Min(1)
    private BigDecimal valorTransferencia;
    private String descricao;
}
