package br.com.dourado.pagamento.simplificado.api.domain.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "conta_corrente", schema = "pagamento_simplificado")
public class ContaCorrente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "saldo", nullable = false, precision = 19, scale = 2)
    private BigDecimal saldo = BigDecimal.ZERO;

    @OneToMany(mappedBy = "contaOrigem", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<HistoricoTransacao> transacoesEnviadas = new HashSet<>();

    @OneToMany(mappedBy = "contaDestino", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<HistoricoTransacao> transacoesRecebidas = new HashSet<>();

    @Column(name = "agencia", nullable = false, length = 20)
    private String agencia;

    @Column(name = "conta_corrente", nullable = false, length = 30)
    private String contaCorrente;

    @Column(name = "dt_criacao", updatable = false)
    private ZonedDateTime dtCriacao = ZonedDateTime.now();

}