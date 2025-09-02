package br.com.dourado.pagamento.simplificado.api.domain.dtos.transferencia;

import br.com.dourado.pagamento.simplificado.api.domain.entities.ContaCorrente;
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
    private String contaCorrente;

    public DadosTransferenciaUsuarioDTO criarDadosTransferenciaUsuarioDTO(ContaCorrente contaCorrente) {
        Usuario usuario = contaCorrente.getUsuario();
        this.nome = usuario.getNome();
        this.cpfCnpj = usuario.getCpfCnpj();
        this.agencia = contaCorrente.getAgencia();
        this.contaCorrente = contaCorrente.getContaCorrente();
        return this;
    }
}
