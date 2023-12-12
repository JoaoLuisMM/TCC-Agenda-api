package br.com.joaoluis.tcc.common.dto;

import java.time.LocalDate;

public record ClienteOut(
        Long id,
        String cpf,
        String nome,
        String email,
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
