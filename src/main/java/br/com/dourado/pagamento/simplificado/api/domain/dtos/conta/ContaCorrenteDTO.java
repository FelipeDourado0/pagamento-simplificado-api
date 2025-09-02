package br.com.dourado.pagamento.simplificado.api.domain.dtos.conta;

import br.com.dourado.pagamento.simplificado.api.domain.entities.ContaCorrente;
import br.com.dourado.pagamento.simplificado.api.domain.entities.HistoricoTransacao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContaCorrenteDTO {

    private String agencia;

    private String contaCorrente;

    private BigDecimal saldo;

    private List<HistoricoTransacaoDTO> transacoesEnviadas = new ArrayList<>();

    private List<HistoricoTransacaoDTO> transacoesRecebidas = new ArrayList<>();

    public ContaCorrenteDTO fromContaCorrenteEntity(ContaCorrente contaCorrenteEntity, List<HistoricoTransacao> listaHistoricoTransacao) {
        this.agencia = contaCorrenteEntity.getAgencia();
        this.contaCorrente = contaCorrenteEntity.getContaCorrente();
        this.saldo = contaCorrenteEntity.getSaldo();
        this.transacoesEnviadas = listaHistoricoTransacao
                .stream()
                .filter(it -> Objects.equals(it.getContaOrigem().getId(), contaCorrenteEntity.getId()))
                .map(it -> new HistoricoTransacaoDTO().fromHistoricoTransacaoEntity(it))
                .toList();

        this.transacoesRecebidas = contaCorrenteEntity.getTransacoesRecebidas()
                .stream()
                .filter(it -> Objects.equals(it.getContaDestino().getId(), contaCorrenteEntity.getId()))
                .map(it -> new HistoricoTransacaoDTO().fromHistoricoTransacaoEntity(it))
                .toList();

        return this;
    }
}
