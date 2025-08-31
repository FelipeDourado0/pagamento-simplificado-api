package br.com.dourado.pagamento.simplificado.api.domain.repositories;

import br.com.dourado.pagamento.simplificado.api.domain.entities.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PerfilRepository extends JpaRepository<Perfil, Long> {

    public Optional<Perfil> findByNome(String nome);
}
