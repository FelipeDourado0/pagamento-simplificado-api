package br.com.dourado.pagamento.simplificado.api.domain.dtos.conta;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DadosContaResponseDTO {
    UsuarioDTO dadosUsuario;
    ContaDTO dadosConta;
}
