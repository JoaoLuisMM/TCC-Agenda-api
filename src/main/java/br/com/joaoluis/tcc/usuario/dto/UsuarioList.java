package br.com.joaoluis.tcc.usuario.dto;

import br.com.joaoluis.tcc.usuario.model.PapelUsuario;
import br.com.joaoluis.tcc.usuario.model.Usuario;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class UsuarioList implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String nome;
    private String email;
    private Boolean ativo;
    private boolean administrador;
    private PapelUsuario papel;
    private String empresa;

    public UsuarioList() {
    }

    public UsuarioList(Usuario usuario) {
        this.id = usuario.getId();
        this.nome = usuario.getNome();
        this.ativo = usuario.getEnabled();
        this.administrador = usuario.isAdministrador();
        this.papel = usuario.getPapel();
        this.empresa = usuario.getEmpresa().getNome();
    }
}
