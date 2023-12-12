package br.com.joaoluis.tcc.agendamento.dto;

import br.com.joaoluis.tcc.common.model.Cliente;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class ClienteDto {

    private Long id;
    private String nome;
    private String email;
    private String celular;

    public ClienteDto(){}

    public ClienteDto(Cliente cliente){
        this.id = cliente.getId();
        this.nome = cliente.getNome();
        this.email = cliente.getEmail();
        this.celular = cliente.getCelular();
    }
}
