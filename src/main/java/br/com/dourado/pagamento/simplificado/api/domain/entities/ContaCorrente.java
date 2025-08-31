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
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "conta_corrente", schema = "pagamento_simplificado")
public class ContaCorrente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "saldo", nullable = false, precision = 19, scale = 2)
    private BigDecimal saldo = BigDecimal.ZERO;

    @Column(name = "dt_criacao", updatable = false)
    private Instant dtCriacao = Instant.now();

    @OneToMany(mappedBy = "contaOrigem", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<HistoricoTransacao> transacoesEnviadas = new ArrayList<>();

    @OneToMany(mappedBy = "contaDestino", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<HistoricoTransacao> transacoesRecebidas = new ArrayList<>();
}