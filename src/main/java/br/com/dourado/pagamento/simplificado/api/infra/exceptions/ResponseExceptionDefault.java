package br.com.dourado.pagamento.simplificado.api.infra.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseExceptionDefault {
    private String titulo;
    private String descicao;
}
