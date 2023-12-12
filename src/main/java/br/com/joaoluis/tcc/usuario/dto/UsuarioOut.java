package br.com.joaoluis.tcc.usuario.dto;

import br.com.joaoluis.tcc.empresa.model.Empresa;
import br.com.joaoluis.tcc.usuario.model.PapelUsuario;
import br.com.joaoluis.tcc.usuario.model.Usuario;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

import java.io.Serializable;

@Setter
@Getter
public class UsuarioOut implements Serializable {

    private Long id;
    @NotBlank(message = "O nome é obrigatório")
    private String nome;
    @Email(message = "O e-mail deve ser válido")
    @NotBlank(message = "O e-mail é obrigatório")
    private String email;
    @NotBlank(message = "O CPF é obrigatório")
    @CPF(message = "O CPF é obrigatório ser válido")
    private String cpf;
    @Valid
    @NotNull(message = "A empresa é obrigatória")
    private EmpresaId empresa;
    @NotNull(message = "O papel do usuário é obrigatório")
    private PapelUsuario papel;

    public UsuarioOut() {
    }

    public static UsuarioOut of(Usuario usuario) {
        UsuarioOut dto = new UsuarioOut();
        dto.setId(usuario.getId());
        dto.setCpf(usuario.getCpf());
        dto.setNome(usuario.getNome());
        dto.setEmpresa(new EmpresaId(usuario.getEmpresa().getId()));
        dto.papel = usuario.getPapel();
        return dto;
    }

    public Usuario toEntity() {
        Usuario usuario = new Usuario();
        usuario.setId(this.getId());
        usuario.setCpf(this.getCpf());
        usuario.setNome(this.getNome());
        usuario.setEnabled(true);
        usuario.setAccountExpired(false); //this.getAccountExpired());
        usuario.setAccountLocked(false); //this.getAccountLocked());
        usuario.setPasswordExpired(false); //this.getPasswordExpired());
        usuario.setEmpresa(Empresa.builder().id(this.empresa.getId()).build());
        return usuario;
    }
}
