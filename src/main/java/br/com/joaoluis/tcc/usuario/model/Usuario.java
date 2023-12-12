package br.com.joaoluis.tcc.usuario.model;

import br.com.joaoluis.tcc.empresa.model.Empresa;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.Set;

@Setter
@Getter
@Entity
@Builder
@AllArgsConstructor
@Table(name="usuario", uniqueConstraints = { @UniqueConstraint(columnNames = { "cpf" }) })
public class Usuario {

	@Id
	@SequenceGenerator(sequenceName="usuario_id_seq", name="usuario_id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usuario_id_seq")
	private Long id;
	@Column(length = 250, nullable = false)
	private String nome;
	@Column(length = 11, unique = true, nullable = false)
	private String cpf;
	@Column(length = 100, unique = true)
	private String email;
	@Column(length = 11)
	private String celular;

	@Column(length = 250, nullable = false)
	private String password;
	@Column(name = "account_expired")
	private Boolean accountExpired;
	@Builder.Default
	@Column(name = "account_locked")
	private Boolean accountLocked = false;
	@Builder.Default
	private Boolean enabled = true;
	@Column(name = "password_expired")
	private Boolean passwordExpired;

	@ManyToOne
	@JoinColumn(name = "empresa_id", referencedColumnName = "id", nullable = false)
	private Empresa empresa;

	@Enumerated(EnumType.STRING)
	@Column(length = 30, nullable = false)
	private PapelUsuario papel;

	public Usuario() {
	}

	public Set<String> getAuthorities() {
		return Set.of(this.papel.name());
	}

	public Boolean isAdministrador() {
        return this.papel.equals(PapelUsuario.ROLE_ADMINISTRADOR);
    }

	public Boolean isOperacional() {
        return this.papel.equals(PapelUsuario.ROLE_OPERACIONAL);
    }

	public Long getEmpresaId() {
		return this.empresa.getId();
	}

	@Override
	public String toString() {
		return "Usuario{" +
//				"id=" + id +
				", nome='" + nome + '\'' +
				", cpf='" + cpf + '\'' +
				", empresa=" + empresa +
				", papel=" + papel +
				", password='" + password + '\'' +
				", accountExpired=" + accountExpired +
				", accountLocked=" + accountLocked +
				", enabled=" + enabled +
				", passwordExpired=" + passwordExpired +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Usuario usuario = (Usuario) o;
		return id.equals(usuario.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
