package br.com.dourado.pagamento.simplificado.api.domain.repositories;

import br.com.dourado.pagamento.simplificado.api.domain.entities.ContaCorrente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ContaCorrenteRepository extends JpaRepository<ContaCorrente, Long> {


    Optional<ContaCorrente> findByUsuarioEmail(@Param("email") String email);

    @Query("""
                SELECT cc FROM ContaCorrente cc
                JOIN FETCH cc.usuario u
                LEFT JOIN FETCH cc.transacoesEnviadas te
                LEFT JOIN FETCH cc.transacoesRecebidas tr
                WHERE u.cpfCnpj = :cpfCnpj
            """)
    Optional<ContaCorrente> findByUsuarioCpfCnpj(@Param("cpfCnpj") String cpfCnpj);
}
