package br.com.joaoluis.tcc.usuario.dto;

import br.com.joaoluis.tcc.usuario.model.PapelUsuario;
import br.com.joaoluis.tcc.usuario.model.Usuario;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.br.CPF;

import java.io.Serializable;

@Setter
@Getter
@ToString
public class UsuarioEdit implements Serializable {

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

    public UsuarioEdit() {
    }

    public void merge(Usuario usuario) {
        usuario.setNome(this.nome);
        usuario.setNome(this.email);
        usuario.setPapel(this.papel);
        usuario.setCpf(this.getCpf());
    }

    public String getCpf() {
        return this.cpf != null ? this.cpf.replaceAll("[^0-9]", "") : null;
    }
}
