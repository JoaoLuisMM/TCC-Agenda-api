package br.com.joaoluis.tcc.common.validation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.br.CPF;

@Getter
@Builder
public class CPFValidator {
    @CPF(message = "CPF inválido. Por favor, informe corretamente.")
    @NotBlank(message = "O CPF é obrigatório.")
    @Size(min = 11, max = 11, message = "O CPF deve conter 11 caracteres.")
    private String cpf;
}
