package br.com.dourado.pagamento.simplificado.api.domain.dtos.transferencia;

import br.com.dourado.pagamento.simplificado.api.domain.entities.ContaCorrente;
import br.com.dourado.pagamento.simplificado.api.domain.entities.HistoricoTransacao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExtratoTransferenciaDTO {
    private Long idTransacao;
    private DadosTransferenciaUsuarioDTO dadosPagador;
    private DadosTransferenciaUsuarioDTO dadosRecebedor;
    private DadosHistoricoTransferenciaDTO dadosHistorico;

    public ExtratoTransferenciaDTO criarExtratoTransfereciaDto(ContaCorrente contaCorrentePagador, ContaCorrente contaCorrenteRecebedor, HistoricoTransacao historicoTransacao) {

        this.idTransacao = historicoTransacao.getId();
        this.dadosPagador = new DadosTransferenciaUsuarioDTO().criarDadosTransferenciaUsuarioDTO(contaCorrentePagador);
        this.dadosRecebedor = new DadosTransferenciaUsuarioDTO().criarDadosTransferenciaUsuarioDTO(contaCorrenteRecebedor);
        this.dadosHistorico = new DadosHistoricoTransferenciaDTO().criarDadosHistoricoTransferenciaDTO(historicoTransacao);

        return this;
    }
}
