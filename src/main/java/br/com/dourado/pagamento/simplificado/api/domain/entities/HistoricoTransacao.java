package br.com.dourado.pagamento.simplificado.api.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "historico_transacao", schema = "pagamento_simplificado")
public class HistoricoTransacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "conta_origem_id", nullable = false)
    private ContaCorrente contaOrigem;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "conta_destino_id", nullable = false)
    private ContaCorrente contaDestino;

    @Column(name = "dt_envio_transacao", nullable = false)
    private ZonedDateTime dtEnvioTransacao;

    @Column(name = "transacao_concluida", nullable = false)
    private boolean transacaoConcluida = false;

    @Column(name = "mensagem_erro")
    private String mensagemErro;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "valor", precision = 19, scale = 4)
    private BigDecimal valor;

    @Column(name = "dt_criacao", updatable = false)
    private ZonedDateTime dtCriacao = ZonedDateTime.now();
}
