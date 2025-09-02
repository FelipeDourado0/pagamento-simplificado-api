package br.com.dourado.pagamento.simplificado.api.domain.dtos.autenticacao;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AutenticacaoRequestDTO {
    @NonNull
    @Email
    private String email;
    @NonNull
    private String senha;
}
