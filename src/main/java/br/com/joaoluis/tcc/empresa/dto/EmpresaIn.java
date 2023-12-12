package br.com.joaoluis.tcc.empresa.dto;

import br.com.joaoluis.tcc.empresa.model.Empresa;
import br.com.joaoluis.tcc.empresa.model.RamoAtividade;
import br.com.joaoluis.tcc.empresa.model.TipoPessoa;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record EmpresaIn(
        @NotBlank(message = "O nome da empresa é obrigatório")
        String nome,
        String cnpjCpf, // validado na classe EmpresaValidation
        @NotNull TipoPessoa tipoPessoa,
        @Size(min = 10, max = 11, message = "O número do celular deve conter 10 ou 11 dígitos")
        String celular,
        @Size(min = 10, max = 11, message = "O número do telefone deve conter 10 ou 11 dígitos")
        String telefone,
        @NotNull(message = "O e-mail é obrigatório")
        @Email(message = "O e-mail deve ser válido")
        String email,
        @NotNull(message = "O ramo de atividade é obrigatório")
        RamoAtividade ramoAtividade,
        @NotNull(message = "O responsável é obrigatório")
        @Valid
        ResponsavelIn responsavel) {

    public Empresa toEntity() {
        return Empresa.builder()
                .nome(this.nome)
                .cnpjCpf(this.cnpjCpf)
                .tipoPessoa(this.tipoPessoa)
                .celular(this.celular)
                .telefone(this.telefone)
                .email(this.email)
                .ramoAtividade(this.ramoAtividade)
                .build();
    }

    public boolean isPessoaJuridica() {
        return TipoPessoa.PESSOA_JURIDICA.equals(this.tipoPessoa);
    }
}
