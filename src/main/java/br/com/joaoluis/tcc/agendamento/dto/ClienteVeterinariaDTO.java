package br.com.joaoluis.tcc.agendamento.dto;

import br.com.joaoluis.tcc.clinicaveterinaria.dto.TutorDto;
import br.com.joaoluis.tcc.clinicaveterinaria.model.ClienteVeterinaria;
import br.com.joaoluis.tcc.common.model.Cliente;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ClienteVeterinariaDTO {
    private Long id;
    private String nome;
    private String email;
    private String celular;
    private TutorDto tutor;

    public ClienteVeterinariaDTO(Cliente cliente) {
        this.id = cliente.getId();
        this.nome = cliente.getNome();
        this.email = cliente.getEmail();
        this.celular = cliente.getCelular();
        ClienteVeterinaria c = (ClienteVeterinaria) cliente;
        this.tutor = new TutorDto(c.getTutor().getId(),
                c.getTutor().getNome(), c.getTutor().getEmail(),
                c.getTutor().getCelular());
    }
}
