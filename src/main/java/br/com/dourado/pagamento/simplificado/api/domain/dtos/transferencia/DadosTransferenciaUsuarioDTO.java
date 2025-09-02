package br.com.dourado.pagamento.simplificado.api.domain.dtos.transferencia;

import br.com.dourado.pagamento.simplificado.api.domain.entities.Conta;
import br.com.dourado.pagamento.simplificado.api.domain.entities.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DadosTransferenciaUsuarioDTO {
    private String nome;
    private String cpfCnpj;
    private String agencia;
    private String numeroConta;

    public DadosTransferenciaUsuarioDTO criarDadosTransferenciaUsuarioDTO(Conta conta) {
        Usuario usuario = conta.getUsuario();
        this.nome = usuario.getNome();
        this.cpfCnpj = usuario.getCpfCnpj();
        this.agencia = conta.getAgencia();
        this.numeroConta = conta.getNumeroConta();
        return this;
    }
}
