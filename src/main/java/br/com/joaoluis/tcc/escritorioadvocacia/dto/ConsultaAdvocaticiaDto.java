package br.com.joaoluis.tcc.escritorioadvocacia.dto;

import br.com.joaoluis.tcc.agendamento.dto.ClienteDto;
import br.com.joaoluis.tcc.agendamento.dto.EspecialistaDto;
import br.com.joaoluis.tcc.agendamento.model.Agendamento;
import br.com.joaoluis.tcc.escritorioadvocacia.model.AgendamentoAdvocaticio;
import br.com.joaoluis.tcc.escritorioadvocacia.model.TipoCompromisso;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Setter
@Getter
@Builder
public class ConsultaAdvocaticiaDto implements Serializable {

    private long id;
    private ClienteDto cliente;
    private EspecialistaDto especialista;
    private String situacao;
    private LocalDateTime dataInicial;
    private LocalDateTime dataFinal;
    private String numeroProcesso;
    private TipoCompromisso tipoCompromisso;
    private String comarca;

    public static ConsultaAdvocaticiaDto of(AgendamentoAdvocaticio agendamento) {
        return ConsultaAdvocaticiaDto.builder()
                .id(agendamento.getId())
                .cliente(new ClienteDto(agendamento.getCliente()))
                .especialista(new EspecialistaDto(agendamento.getEspecialista()))
                .situacao(agendamento.getSituacao().name())
                .dataInicial(agendamento.getDataInicial())
                .dataFinal(agendamento.getDataFinal())
                .numeroProcesso(agendamento.getNumeroProcesso())
                .tipoCompromisso(agendamento.getTipoCompromisso())
                .comarca(agendamento.getComarca())
                .build();
    }
}
