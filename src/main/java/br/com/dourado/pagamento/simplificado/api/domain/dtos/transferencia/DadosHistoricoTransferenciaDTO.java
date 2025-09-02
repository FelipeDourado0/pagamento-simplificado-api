package br.com.dourado.pagamento.simplificado.api.domain.dtos.transferencia;

import br.com.dourado.pagamento.simplificado.api.domain.entities.HistoricoTransacao;
import br.com.dourado.pagamento.simplificado.api.domain.enums.StatusTransferenciaEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DadosHistoricoTransferenciaDTO {
    private ZonedDateTime dataTransferencia;
    private BigDecimal valorTransferencia;
    private StatusTransferenciaEnum status;
    private String descricao;

    public DadosHistoricoTransferenciaDTO criarDadosHistoricoTransferenciaDTO(HistoricoTransacao historicoTransacao) {
        this.dataTransferencia = historicoTransacao.getDtEnvioTransacao();
        this.valorTransferencia = historicoTransacao.getValor();
        this.status = historicoTransacao.isTransacaoConcluida() ? StatusTransferenciaEnum.EFETUADA : StatusTransferenciaEnum.RECUSADA;
        this.descricao = historicoTransacao.getDescricao();

        return this;
    }
}
