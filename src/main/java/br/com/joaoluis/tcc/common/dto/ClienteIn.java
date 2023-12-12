package br.com.joaoluis.tcc.common.dto;

import br.com.joaoluis.tcc.common.model.Cliente;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

public record ClienteIn(
        @NotBlank(message = "O CPF é obrigatório")
        @CPF(message = "O CPF deve ser válido")
        String cpf,
        @NotBlank(message = "O nome é obrigatório")
        String nome,
        @Email(message = "O e-mail deve ser válido")
        @NotBlank(message = "O e-mail é obrigatório")
        String email,
        LocalDate dataNascimento,
//        @Size(min = 10, max = 11, message = "O número de celular deve ter 10 ou 11 caracteres")
        String celular,
        @Size(max = 120, message = "A rua deve ter no máximo 120 caracteres")
        String rua,
        @Size(max = 40, message = "O número da rua deve ter no máximo 40 caracteres")
        String numero,
        @Size(max = 100, message = "O complemento do endereço deve ter no máximo 100 caracteres")
        String complemento,
        @Size(min = 8, max = 8, message = "O CEP deve ter no máximo 8 caracteres")
        String cep,
        @Size(max = 80, message = "O bairro deve ter no máximo 80 caracteres")
        String bairro,
        @Size(max = 60, message = "A cidade deve ter no máximo 60 caracteres")
        String cidade
) {
    public Cliente toEntity() {
        return Cliente.builder()
                .cpf(this.cpf)
                .nome(this.nome)
                .email(this.email)
                .dataNascimento(this.dataNascimento)
                .celular(this.celular)
                .rua(this.rua)
                .numero(this.numero)
                .complemento(this.complemento)
                .cep(this.cep)
                .bairro(this.bairro)
                .cidade(this.cidade)
                .inativo(false)
                .build();
    }
}
