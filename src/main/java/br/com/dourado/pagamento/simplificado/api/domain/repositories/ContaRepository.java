package br.com.dourado.pagamento.simplificado.api.domain.repositories;

import br.com.dourado.pagamento.simplificado.api.domain.entities.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ContaRepository extends JpaRepository<Conta, Long> {


    Optional<Conta> findByUsuarioEmail(@Param("email") String email);

    @Query("""
                SELECT cc FROM Conta cc
                JOIN FETCH cc.usuario u
                LEFT JOIN FETCH cc.transacoesEnviadas te
                LEFT JOIN FETCH cc.transacoesRecebidas tr
                WHERE u.cpfCnpj = :cpfCnpj
            """)
    Optional<Conta> findByUsuarioCpfCnpj(@Param("cpfCnpj") String cpfCnpj);
}
