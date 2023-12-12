package br.com.joaoluis.tcc.agendamento.dto;

import br.com.joaoluis.tcc.usuario.model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class EspecialistaDto {
    private Long id;
    private String nome;
    private String email;
    private String celular;

    public EspecialistaDto(){}

    public EspecialistaDto(Usuario especialista){
        this.id = especialista.getId();
        this.nome = especialista.getNome();
        this.email = especialista.getEmail();
        this.celular = especialista.getCelular();
    }
}
