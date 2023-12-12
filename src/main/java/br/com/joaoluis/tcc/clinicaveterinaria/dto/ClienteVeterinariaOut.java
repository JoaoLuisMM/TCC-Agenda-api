package br.com.joaoluis.tcc.clinicaveterinaria.dto;

import br.com.joaoluis.tcc.clinicaveterinaria.model.ClienteVeterinaria;
import br.com.joaoluis.tcc.clinicaveterinaria.model.Sexo;

import java.time.LocalDate;

public record ClienteVeterinariaOut(
        Long id,
        String nome,
        LocalDate dataNascimento,
        String raca,
        Sexo sexo,
        EspecieClienteDto especie,
        String nomeTutor,
        String cpf,
        String email,
        String celular
) {
    public static ClienteVeterinariaOut of(ClienteVeterinaria cliente) {
        return new ClienteVeterinariaOut(
                cliente.getId(),
                cliente.getNome(),
                cliente.getDataNascimento(),
                cliente.getRaca(),
                cliente.getSexo(),
                new EspecieClienteDto(cliente.getEspecie().getId(), cliente.getEspecie().getDescricao()),
                cliente.getTutor().getNome(),
                cliente.getCpf(),
                cliente.getEmail(),
                cliente.getCelular()
        );
    }
}
