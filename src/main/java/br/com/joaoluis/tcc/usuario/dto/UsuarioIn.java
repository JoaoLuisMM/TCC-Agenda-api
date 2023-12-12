package br.com.joaoluis.tcc.usuario.dto;

import br.com.joaoluis.tcc.empresa.model.Empresa;
import br.com.joaoluis.tcc.usuario.model.Usuario;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Setter
@Getter
@ToString
public class UsuarioIn extends UsuarioOut implements Serializable {

    @NotBlank(message = "A senha é obrigatória")
    private String password;

    public UsuarioIn() {
    }

    public Usuario toEntity() {
        Usuario usuario = new Usuario();
        usuario.setNome(this.getNome());
        usuario.setAccountExpired(false);
        usuario.setAccountLocked(false);
        usuario.setPasswordExpired(false);
        usuario.setEnabled(true);
        usuario.setEmpresa(Empresa.builder().id(this.getEmpresa().getId()).build());
        return usuario;
    }
}
