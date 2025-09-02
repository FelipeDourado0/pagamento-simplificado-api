package br.com.dourado.pagamento.simplificado.api.domain.client;

import br.com.dourado.pagamento.simplificado.api.domain.dtos.autorizacaoTransacao.AutorizacaoTransacaoResponseDTO;
import br.com.dourado.pagamento.simplificado.api.infra.helper.LoggerHelper;
import br.com.dourado.pagamento.simplificado.api.infra.helper.WebClientHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AutorizacaoTransacaoClient {
    @Value("${api.url.authorize.transacao}")
    private String url;
    @Autowired
    private LoggerHelper loggerHelper;
    @Autowired
    private WebClientHelper webClientHelper;

    public AutorizacaoTransacaoResponseDTO obterStatusDeAutorizacaoParaTransacao() {
        return webClientHelper
                .get(url, AutorizacaoTransacaoResponseDTO.class)
                .block();
    }
}
