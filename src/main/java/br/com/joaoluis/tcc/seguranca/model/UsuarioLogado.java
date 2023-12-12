package br.com.joaoluis.tcc.seguranca.model;

import br.com.joaoluis.tcc.usuario.model.Usuario;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UsuarioLogado {

    private Long id;
    private String nome;
    private String email;
    private EmpresaResumo empresa;
    private String permissao;

    public UsuarioLogado() {
    }

    public UsuarioLogado(UsuarioSistema usuario) {
        this.id = usuario.getId();
        this.nome = usuario.getNome();
        this.email = usuario.getEmail();
        this.empresa = usuario.getEmpresa();
        this.permissao = usuario.getPermissao();
    }

    public boolean isAdministrador() {
        return "ROLE_ADMINISTRADOR".equals(this.permissao);
    }

    public Usuario toUsuario() {
        Usuario usuario = new Usuario();
        usuario.setId(this.id);
        return usuario;
    }

    @Override
    public String toString() {
        return "UsuarioSistema{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", empresa='" + empresa + '\'' +
                ", permissao=" + permissao +
                '}';
    }
}
