package br.com.dourado.pagamento.simplificado.api.domain.dtos.conta;

import br.com.dourado.pagamento.simplificado.api.domain.entities.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {
    private String nome;

    private String email;

    private String cpfCnpj;

    private boolean pessoaJuridica;

    private boolean ativo;

    public UsuarioDTO fromUsuarioEntity(Usuario usuarioEntity) {
        this.nome = usuarioEntity.getNome();
        this.cpfCnpj = usuarioEntity.getCpfCnpj();
        this.pessoaJuridica = usuarioEntity.isPessoaJuridica();
        this.ativo = usuarioEntity.isAtivo();
        this.email = usuarioEntity.getEmail();

        return this;
    }
}
