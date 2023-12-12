package br.com.joaoluis.tcc.agendamento.dto;

import br.com.joaoluis.tcc.agendamento.model.Agendamento;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Setter
@Getter
@Builder
public class ConsultaVeterinariaDto implements Serializable {

    private long id;
    private ClienteVeterinariaDTO cliente;
    private EspecialistaDto especialista;
    private String situacao;
    private LocalDateTime dataInicial;
    private LocalDateTime dataFinal;

    public static ConsultaVeterinariaDto of(Agendamento agendamento) {
        return ConsultaVeterinariaDto.builder()
                .id(agendamento.getId())
                .cliente(new ClienteVeterinariaDTO(agendamento.getCliente()))
                .especialista(new EspecialistaDto(agendamento.getEspecialista()))
                .situacao(agendamento.getSituacao().name())
                .dataInicial(agendamento.getDataInicial())
                .dataFinal(agendamento.getDataFinal())
                .build();
    }
}
