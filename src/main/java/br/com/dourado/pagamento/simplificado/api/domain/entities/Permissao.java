package br.com.dourado.pagamento.simplificado.api.domain.entities;

import br.com.dourado.pagamento.simplificado.api.domain.enums.UsuarioRoleEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.ZonedDateTime;

@Entity
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "permissao", schema = "pagamento_simplificado")
public class Permissao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true, length = 100)
    private UsuarioRoleEnum role;

    private String descricao;

    @Column(name = "dt_criacao", updatable = false)
    private ZonedDateTime dtCriacao = ZonedDateTime.now();
}