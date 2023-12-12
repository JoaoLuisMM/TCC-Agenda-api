package br.com.joaoluis.tcc.empresa.dto;

import br.com.joaoluis.tcc.usuario.model.Usuario;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;

public record ResponsavelIn(
        @NotBlank(message = "O nome do responsável é obrigatório")
        String nome,
        @NotNull(message = "O CPF do responsável é obrigatório")
        @CPF(message = "CPF do responsável é inválido. Por favor, informe corretamente")
        @Size(min = 11, max = 11, message = "O CPF do responsável deve conter 11 dígitos")
        String cpf,
        @NotNull(message = "A senha do responsável é obrigatório")
        String senha
) {
    public Usuario toEntity() {
        return Usuario.builder()
                .cpf(this.cpf)
                .nome(this.nome)
                .build();
    }
}
