package br.com.joaoluis.tcc.common.dto;

import br.com.joaoluis.tcc.usuario.model.PapelUsuario;
import br.com.joaoluis.tcc.usuario.model.Usuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;

public record FuncionarioIn(
        @NotBlank(message = "O nome é obrigatório")
        String nome,
        @NotBlank(message = "O CPF é obrigatório")
        @CPF(message = "O CPF deve ser válido")
        String cpf,
        @Email(message = "O e-mail deve ser válido")
        @NotBlank(message = "O e-mail é obrigatório")
        String email,
        String celular,
        @NotBlank(message = "A permissão é obrigatória")
        String papel
        ) {

    public Usuario toEntity() {
        return Usuario.builder()
                .nome(this.nome)
                .cpf(this.cpf)
                .email(this.email)
                .celular(this.celular)
                .papel(PapelUsuario.valueOf(this.papel))
                .build();
    }
}
