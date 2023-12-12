package br.com.joaoluis.tcc.clinicaveterinaria.dto;

import br.com.joaoluis.tcc.clinicaveterinaria.model.ClienteVeterinaria;
import br.com.joaoluis.tcc.clinicaveterinaria.model.EspecieCliente;
import br.com.joaoluis.tcc.clinicaveterinaria.model.Sexo;
import br.com.joaoluis.tcc.clinicaveterinaria.model.Tutor;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

public record ClienteVeterinariaIn(
        @NotBlank(message = "O nome é obrigatório")
        String nome,
        LocalDate dataNascimento,
        String raca,
        Sexo sexo,
        EspecieClienteDto especie,
        @NotBlank(message = "O nome do tutor é obrigatório")
        String nomeTutor,
        @NotBlank(message = "O cpf do tutor é obrigatório")
        @CPF(message = "O cpf do tutor deve ser válido")
        String cpf,
        String email,
        String celular
) {
    public ClienteVeterinaria toEntity() {
        return ClienteVeterinaria.clienteVeterinariaBuilder()
                .nome(this.nome)
                .cpf(this.cpf)
                .celular(this.celular)
                .email(this.email)
                .dataNascimento(this.dataNascimento)
                .raca(this.raca)
                .sexo(this.sexo)
                .inativo(false)
                .especie(EspecieCliente.builder().id(this.especie.id()).build())
                .tutor(Tutor.builder()
                        .nome(this.nomeTutor)
                        .cpf(this.cpf)
                        .email(this.email)
                        .celular(this.celular)
                        .build())
                .build();
    }

    public void merge(ClienteVeterinaria cliente) {
        cliente.setNome(this.nome);
        cliente.setCpf(this.cpf);
        cliente.setCelular(this.celular);
        cliente.setEmail(this.email);
        cliente.setDataNascimento(this.dataNascimento);
        cliente.setRaca(this.raca);
        cliente.setSexo(this.sexo);
        cliente.setEspecie(EspecieCliente.builder().id(this.especie.id()).build());
//        final Tutor tutor = cliente.getTutor();
//        tutor.setNome(this.nomeTutor);
//        tutor.setCpf(this.cpf);
//        tutor.setEmail(this.email);
//        tutor.setCelular(this.celular);
    }
}
