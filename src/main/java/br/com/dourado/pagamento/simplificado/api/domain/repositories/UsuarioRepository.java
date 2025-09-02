package br.com.dourado.pagamento.simplificado.api.domain.repositories;

import br.com.dourado.pagamento.simplificado.api.domain.entities.Usuario;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END " +
            "FROM Usuario u WHERE u.email = :email OR u.cpfCnpj = :cpfCnpj")
    boolean existsByEmailOrCpfCnpj(@Param("email") String email, @Param("cpfCnpj") String cpfCnpj);

    @EntityGraph(attributePaths = {"perfil", "perfil.roleList", "perfil.roleList.role"})
    Optional<Usuario> findByEmail(String email);
}
