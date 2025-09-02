package br.com.dourado.pagamento.simplificado.api.service;

import br.com.dourado.pagamento.simplificado.api.domain.dtos.transferencia.ExtratoTransferenciaDTO;
import br.com.dourado.pagamento.simplificado.api.domain.dtos.transferencia.TransferenciaRequestDTO;
import br.com.dourado.pagamento.simplificado.api.domain.entities.Conta;
import br.com.dourado.pagamento.simplificado.api.domain.entities.HistoricoTransacao;
import br.com.dourado.pagamento.simplificado.api.domain.repositories.ContaRepository;
import br.com.dourado.pagamento.simplificado.api.domain.repositories.UsuarioRepository;
import br.com.dourado.pagamento.simplificado.api.infra.exceptions.AccessDeniedException;
import br.com.dourado.pagamento.simplificado.api.infra.exceptions.BadRequestExeption;
import br.com.dourado.pagamento.simplificado.api.infra.exceptions.NotFoundExeption;
import br.com.dourado.pagamento.simplificado.api.infra.helper.LoggerHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class TransferenciaService {
    @Autowired
    private LoggerHelper loggerHelper;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ContaRepository contaRepository;
    @Autowired
    private HistoricoTransacaoService historicoTransacaoService;


    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ExtratoTransferenciaDTO realizarTransacao(TransferenciaRequestDTO transferenciaReq) {
        loggerHelper.info(this.getClass(), "Processamento de transferência iniciada - " + ZonedDateTime.now());
        loggerHelper.info(this.getClass(), transferenciaReq.toString());

        validarTransferencia(transferenciaReq);

        try {
            Conta contaOrigem = contaRepository.findByUsuarioEmail(transferenciaReq.getEmailOrigem())
                    .orElseThrow(() -> new NotFoundExeption("Usuário não encontrado.", "Usuário origem não encontrado."));

            Conta contaDestino = contaRepository.findByUsuarioEmail(transferenciaReq.getEmailDestino())
                    .orElseThrow(() -> new NotFoundExeption("Usuário não encontrado.", "Usuário destino não encontrado."));

            verificaSeUsuarioOrigemPodeFazerTransacao(contaOrigem, contaDestino, transferenciaReq.getValorTransferencia());

            HistoricoTransacao historicoTransacaoSalvo = concluirTransferencia(
                    contaOrigem,
                    contaDestino,
                    transferenciaReq.getValorTransferencia(),
                    transferenciaReq.getDescricao()
            );

            loggerHelper.info(this.getClass(), "Transferência concluída com sucesso - " + ZonedDateTime.now());
            return new ExtratoTransferenciaDTO().criarExtratoTransfereciaDto(contaOrigem, contaDestino, historicoTransacaoSalvo);

        } catch (Exception e) {
            loggerHelper.error(this.getClass(), "Erro na transferência: " + e.getMessage());
            throw e;
        }
    }

    private void validarTransferencia(TransferenciaRequestDTO req) {
        if (req.getEmailOrigem().equals(req.getEmailDestino())) {
            throw new IllegalArgumentException("Não é possível transferir para a mesma conta.");
        }
        if (req.getValorTransferencia().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor da transferência deve ser maior que zero.");
        }
    }

    private void verificaSeUsuarioOrigemPodeFazerTransacao(Conta contaOrigem, Conta contaDestino, BigDecimal valorTransferencia) {
        if (Objects.equals(contaOrigem.getUsuario().getId(), contaDestino.getUsuario().getId()))
            throw new BadRequestExeption("Envio não autorizado", "Não é permitido envio de saldo circular.");

        if (contaOrigem.getSaldo().compareTo(valorTransferencia) < 0)
            throw new BadRequestExeption("Envio não autorizado", "Saldo insuficiente.");

        if (contaOrigem.getUsuario().isPessoaJuridica())
            throw new AccessDeniedException("Envio não autorizado", "Usuário sem permissão.");
    }

    private HistoricoTransacao concluirTransferencia(Conta contaOrigem, Conta contaDestino, BigDecimal valorTransferencia, String descricaoUsuarioTransferencia) {
        calcularSaldo(contaOrigem, contaDestino, valorTransferencia);
        HistoricoTransacao historicoSalvo = salvarHistoricoTransacao(contaOrigem, contaDestino, valorTransferencia, descricaoUsuarioTransferencia);
        salvarDadosContaCorrente(List.of(contaOrigem, contaDestino));

        return historicoSalvo;
    }

    private HistoricoTransacao salvarHistoricoTransacao(Conta contaOrigem, Conta contaDestino, BigDecimal valorTransferencia, String descricaoUsuarioTransferencia) {
        loggerHelper.info(this.getClass(), "Iniciado criacao de historico - " + ZonedDateTime.now());
        return historicoTransacaoService.criarHistoricoTransacao(contaOrigem, contaDestino, valorTransferencia, descricaoUsuarioTransferencia);
    }

    private void calcularSaldo(Conta contaOrigem, Conta contaDestino, BigDecimal valorTransferencia) {
        contaOrigem.setSaldo(contaOrigem.getSaldo().subtract(valorTransferencia));
        contaDestino.setSaldo(contaDestino.getSaldo().plus().add(valorTransferencia));
    }

    private void salvarDadosContaCorrente(List<Conta> conta) {
        contaRepository.saveAll(conta);
        contaRepository.flush();
    }


}
