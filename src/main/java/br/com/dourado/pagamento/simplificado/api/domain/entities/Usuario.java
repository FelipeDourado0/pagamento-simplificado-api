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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.ZonedDateTime;
import java.util.Collection;

@Entity
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "usuario", schema = "pagamento_simplificado")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "nome", nullable = false, unique = true, length = 150)
    private String nome;

    @Column(unique = true)
    private String email;

    @Column(name = "cpf_cnpj", unique = true, length = 20)
    private String cpfCnpj;

    @Column(name = "pessoa_juridica", nullable = false)
    private boolean pessoaJuridica;

    @Column(nullable = false)
    private String senha;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "perfil_id", nullable = false)
    private Perfil perfil;

    @Column(nullable = false)
    private boolean ativo = true;

    @Column(name = "dt_criacao", updatable = false)
    private ZonedDateTime dtCriacao = ZonedDateTime.now();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.perfil.getRoleList()
                .stream()
                .map(it -> new SimpleGrantedAuthority(it.getRole().getRole()))
                .toList();
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.email;
    }
}