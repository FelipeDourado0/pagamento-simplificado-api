package br.com.dourado.pagamento.simplificado.api.domain.dtos.autorizacaoTransacao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AutorizacaoTransacaoResponseDTO {
    private String status;
    private AutorizacaoResponseDTO data;
}
