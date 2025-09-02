package br.com.dourado.pagamento.simplificado.api.domain.dtos.conta;

import br.com.dourado.pagamento.simplificado.api.domain.entities.Conta;
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
public class ContaDTO {

    private String agencia;

    private String numeroConta;

    private BigDecimal saldo;

    private List<HistoricoTransacaoDTO> transacoesEnviadas = new ArrayList<>();

    private List<HistoricoTransacaoDTO> transacoesRecebidas = new ArrayList<>();

    public ContaDTO fromContaEntity(Conta contaEntity, List<HistoricoTransacao> listaHistoricoTransacao) {
        this.agencia = contaEntity.getAgencia();
        this.numeroConta = contaEntity.getNumeroConta();
        this.saldo = contaEntity.getSaldo();
        this.transacoesEnviadas = listaHistoricoTransacao
                .stream()
                .filter(it -> Objects.equals(it.getContaOrigem().getId(), contaEntity.getId()))
                .map(it -> new HistoricoTransacaoDTO().fromHistoricoTransacaoEntity(it))
                .toList();

        this.transacoesRecebidas = contaEntity.getTransacoesRecebidas()
                .stream()
                .filter(it -> Objects.equals(it.getContaDestino().getId(), contaEntity.getId()))
                .map(it -> new HistoricoTransacaoDTO().fromHistoricoTransacaoEntity(it))
                .toList();

        return this;
    }
}
