package br.com.joaoluis.tcc.common.validation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.br.CNPJ;

@Getter
@Builder
public class CNPJValidator {
    @CNPJ(message = "CNPJ inválido. Por favor, informe corretamente")
    @NotBlank(message = "O CNPJ é obrigatório")
    @Size(min = 14, max = 14, message = "O CNPJ deve conter 14 caracteres")
    private String cnpj;
}
