package br.com.dourado.pagamento.simplificado.api.domain.dtos.conta;

import br.com.dourado.pagamento.simplificado.api.domain.entities.HistoricoTransacao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoricoTransacaoDTO {

    private String nomePagador;

    private String nomeRecebedor;

    private ZonedDateTime dtEnvioTransacao;

    private boolean transacaoConcluida;

    private String mensagemErro;

    private String descricao;

    private BigDecimal valor;

    public HistoricoTransacaoDTO fromHistoricoTransacaoEntity(HistoricoTransacao historicoTransacaoEntity) {
        this.nomePagador = historicoTransacaoEntity.getContaOrigem().getUsuario().getNome();
        this.nomeRecebedor = historicoTransacaoEntity.getContaDestino().getUsuario().getNome();
        this.dtEnvioTransacao = historicoTransacaoEntity.getDtEnvioTransacao();
        this.mensagemErro = historicoTransacaoEntity.getMensagemErro();
        this.descricao = historicoTransacaoEntity.getDescricao();
        this.valor = historicoTransacaoEntity.getValor();
        this.transacaoConcluida = historicoTransacaoEntity.isTransacaoConcluida();

        return this;
    }
}
