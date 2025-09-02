package br.com.dourado.pagamento.simplificado.api.service;

import br.com.dourado.pagamento.simplificado.api.domain.dtos.transferencia.ExtratoTransferenciaDTO;
import br.com.dourado.pagamento.simplificado.api.domain.dtos.transferencia.TransferenciaRequestDTO;
import br.com.dourado.pagamento.simplificado.api.domain.entities.ContaCorrente;
import br.com.dourado.pagamento.simplificado.api.domain.entities.HistoricoTransacao;
import br.com.dourado.pagamento.simplificado.api.domain.repositories.ContaCorrenteRepository;
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
    private ContaCorrenteRepository contaCorrenteRepository;
    @Autowired
    private HistoricoTransacaoService historicoTransacaoService;


    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ExtratoTransferenciaDTO realizarTransacao(TransferenciaRequestDTO transferenciaReq) {
        loggerHelper.info(this.getClass(), "Processamento de transferência iniciada - " + ZonedDateTime.now());
        loggerHelper.info(this.getClass(), transferenciaReq.toString());

        validarTransferencia(transferenciaReq);

        try {
            ContaCorrente contaCorrenteOrigem = contaCorrenteRepository.findByUsuarioEmail(transferenciaReq.getEmailOrigem())
                    .orElseThrow(() -> new NotFoundExeption("Usuário não encontrado.", "Usuário origem não encontrado."));

            ContaCorrente contaCorrenteDestino = contaCorrenteRepository.findByUsuarioEmail(transferenciaReq.getEmailDestino())
                    .orElseThrow(() -> new NotFoundExeption("Usuário não encontrado.", "Usuário destino não encontrado."));

            verificaSeUsuarioOrigemPodeFazerTransacao(contaCorrenteOrigem, contaCorrenteDestino, transferenciaReq.getValorTransferencia());

            HistoricoTransacao historicoTransacaoSalvo = concluirTransferencia(
                    contaCorrenteOrigem,
                    contaCorrenteDestino,
                    transferenciaReq.getValorTransferencia(),
                    transferenciaReq.getDescricao()
            );

            loggerHelper.info(this.getClass(), "Transferência concluída com sucesso - " + ZonedDateTime.now());
            return new ExtratoTransferenciaDTO().criarExtratoTransfereciaDto(contaCorrenteOrigem, contaCorrenteDestino, historicoTransacaoSalvo);

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

    private void verificaSeUsuarioOrigemPodeFazerTransacao(ContaCorrente contaCorrenteOrigem, ContaCorrente contaCorrenteDestino, BigDecimal valorTransferencia) {
        if (Objects.equals(contaCorrenteOrigem.getUsuario().getId(), contaCorrenteDestino.getUsuario().getId()))
            throw new BadRequestExeption("Envio não autorizado", "Não é permitido envio de saldo circular.");

        if (contaCorrenteOrigem.getSaldo().compareTo(valorTransferencia) < 0)
            throw new BadRequestExeption("Envio não autorizado", "Saldo insuficiente.");

        if (contaCorrenteOrigem.getUsuario().isPessoaJuridica())
            throw new AccessDeniedException("Envio não autorizado", "Usuário sem permissão.");
    }

    private HistoricoTransacao concluirTransferencia(ContaCorrente contaCorrenteOrigem, ContaCorrente contaCorrenteDestino, BigDecimal valorTransferencia, String descricaoUsuarioTransferencia) {
        calcularSaldo(contaCorrenteOrigem, contaCorrenteDestino, valorTransferencia);
        HistoricoTransacao historicoSalvo = salvarHistoricoTransacao(contaCorrenteOrigem, contaCorrenteDestino, valorTransferencia, descricaoUsuarioTransferencia);
        salvarDadosContaCorrente(List.of(contaCorrenteOrigem, contaCorrenteDestino));

        return historicoSalvo;
    }

    private HistoricoTransacao salvarHistoricoTransacao(ContaCorrente contaCorrenteOrigem, ContaCorrente contaCorrenteDestino, BigDecimal valorTransferencia, String descricaoUsuarioTransferencia) {
        loggerHelper.info(this.getClass(), "Iniciado criacao de historico - " + ZonedDateTime.now());
        return historicoTransacaoService.criarHistoricoTransacao(contaCorrenteOrigem, contaCorrenteDestino, valorTransferencia, descricaoUsuarioTransferencia);
    }

    private void calcularSaldo(ContaCorrente contaCorrenteOrigem, ContaCorrente contaCorrenteDestino, BigDecimal valorTransferencia) {
        contaCorrenteOrigem.setSaldo(contaCorrenteOrigem.getSaldo().subtract(valorTransferencia));
        contaCorrenteDestino.setSaldo(contaCorrenteDestino.getSaldo().plus().add(valorTransferencia));
    }

    private void salvarDadosContaCorrente(List<ContaCorrente> contaCorrente) {
        contaCorrenteRepository.saveAll(contaCorrente);
        contaCorrenteRepository.flush();
    }


}
