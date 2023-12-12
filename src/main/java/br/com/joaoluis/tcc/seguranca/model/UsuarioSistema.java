package br.com.joaoluis.tcc.seguranca.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Setter
@Getter
public class UsuarioSistema extends User {

    private Long id;
    private String nome;
    private String email;
    private String cpf;
    private EmpresaResumo empresa;
    private Boolean inativo = false;
    private String permissao;

    public UsuarioSistema(String username, String password, boolean enable, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enable, true, true, true, authorities);
    }

    @Override
    public String toString() {
        return "UsuarioSistema{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", empresa='" + empresa + '\'' +
                ", inativo=" + inativo +
                ", enabled=" + this.isEnabled() +
                ", conta nao expirada=" + this.isAccountNonExpired() +
                ", credencial nao expirada=" + this.isCredentialsNonExpired() +
                ", conta nao bloqueada=" + this.isAccountNonLocked() +
                ", permissao=" + permissao +
                '}';
    }
}
