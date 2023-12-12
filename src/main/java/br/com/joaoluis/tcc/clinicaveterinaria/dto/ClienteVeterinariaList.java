package br.com.joaoluis.tcc.clinicaveterinaria.dto;

import java.time.LocalDate;

public record ClienteVeterinariaList(
        Long id,
        String cpf,
        String nome,
        String email,
        EspecieClienteDto especie,
        TutorDto tutor,
        LocalDate dataNascimento,
        String celular,
        String rua,
        String numero,
        String complemento,
        String cep,
        String bairro,
        String cidade
) {
}
