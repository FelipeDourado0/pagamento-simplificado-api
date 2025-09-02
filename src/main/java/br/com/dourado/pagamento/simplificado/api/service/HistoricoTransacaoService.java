package br.com.dourado.pagamento.simplificado.api.service;

import br.com.dourado.pagamento.simplificado.api.domain.client.AutorizacaoTransacaoClient;
import br.com.dourado.pagamento.simplificado.api.domain.constants.Constantes;
import br.com.dourado.pagamento.simplificado.api.domain.dtos.autorizacaoTransacao.AutorizacaoTransacaoResponseDTO;
import br.com.dourado.pagamento.simplificado.api.domain.entities.Conta;
import br.com.dourado.pagamento.simplificado.api.domain.entities.HistoricoTransacao;
import br.com.dourado.pagamento.simplificado.api.domain.repositories.HistoricoTransacaoRepository;
import br.com.dourado.pagamento.simplificado.api.infra.exceptions.BadRequestExeption;
import br.com.dourado.pagamento.simplificado.api.infra.helper.LoggerHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;

@Service
public class HistoricoTransacaoService {
    @Autowired
    private HistoricoTransacaoRepository historicoTransacaoRepository;
    @Autowired
    private AutorizacaoTransacaoClient autorizacaoTransacaoClient;
    @Autowired
    private LoggerHelper loggerHelper;

    public HistoricoTransacao criarHistoricoTransacao(Conta contaOrigem, Conta contaDestino, BigDecimal valorTransferencia, String descricaoUsuarioTransferencia) {
        AutorizacaoTransacaoResponseDTO statusTransacao = verificaStatusTransacaoComApiExterna();
        HistoricoTransacao historicoTransacaoNovo = new HistoricoTransacao()
                .setDtEnvioTransacao(ZonedDateTime.now())
                .setContaDestino(contaDestino)
                .setContaOrigem(contaOrigem)
                .setValor(valorTransferencia)
                .setDescricao(descricaoUsuarioTransferencia)
                .setTransacaoConcluida(statusTransacao.getData().getAuthorization());

        if (Objects.equals(statusTransacao.getStatus(), Constantes.STATUS_TRANSACAO_FALHA)) {
            historicoTransacaoNovo.setMensagemErro(Constantes.MENSAGEM_ERRO_FALHA_TRANSACAO_DEFAULT);
            salvarHistoricoTransacao(historicoTransacaoNovo);
            loggerHelper.info(this.getClass(), "Transferência negada - Historico: " + historicoTransacaoNovo);

            throw new BadRequestExeption("Transferência recusada.", "Transferência negada.");
        }

        loggerHelper.info(this.getClass(), "Finalizado criacao historico - " + ZonedDateTime.now());
        return salvarHistoricoTransacao(historicoTransacaoNovo);
    }

    private AutorizacaoTransacaoResponseDTO verificaStatusTransacaoComApiExterna() {
        loggerHelper.info(this.getClass(), "Iniciada chamada de API externa para validar transferencia - " + ZonedDateTime.now());
        return autorizacaoTransacaoClient.obterStatusDeAutorizacaoParaTransacao();
    }

    private HistoricoTransacao salvarHistoricoTransacao(HistoricoTransacao historicoTransacao) {
        HistoricoTransacao historicoSalvo = historicoTransacaoRepository.save(historicoTransacao);
        historicoTransacaoRepository.flush();

        return historicoSalvo;
    }
}
