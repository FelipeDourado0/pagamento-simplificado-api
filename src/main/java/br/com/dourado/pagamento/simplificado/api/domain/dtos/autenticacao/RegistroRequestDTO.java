package br.com.dourado.pagamento.simplificado.api.domain.dtos.autenticacao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class RegistroRequestDTO {
    private String nome;
    private String senha;
    private String email;
    private String cpfCnpj;
    private Boolean isPessoaJuridica;
    private String perfil;
    private BigDecimal saldoInicial;
}
