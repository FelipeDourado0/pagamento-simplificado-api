package br.com.dourado.pagamento.simplificado.api.domain.repositories;

import br.com.dourado.pagamento.simplificado.api.domain.entities.HistoricoTransacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HistoricoTransacaoRepository extends JpaRepository<HistoricoTransacao, Long> {

    @Query("""
                SELECT t
                FROM HistoricoTransacao t
                JOIN t.contaOrigem co
                JOIN t.contaDestino cd
                WHERE co.usuario.cpfCnpj = :cpfCnpj
                   OR cd.usuario.cpfCnpj = :cpfCnpj
                ORDER BY t.dtEnvioTransacao DESC
            """)
    List<HistoricoTransacao> buscarHistoricoPorCpf(@Param("cpfCnpj") String cpfCnpj);
}
