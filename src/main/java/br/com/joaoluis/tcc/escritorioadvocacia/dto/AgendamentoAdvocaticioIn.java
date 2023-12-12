package br.com.joaoluis.tcc.escritorioadvocacia.dto;

import br.com.joaoluis.tcc.agendamento.dto.AgendamentoIn;
import br.com.joaoluis.tcc.agendamento.dto.ClienteId;
import br.com.joaoluis.tcc.agendamento.dto.EspecialistaId;
import br.com.joaoluis.tcc.agendamento.model.Agendamento;
import br.com.joaoluis.tcc.agendamento.model.SituacaoAgendamento;
import br.com.joaoluis.tcc.common.model.Cliente;
import br.com.joaoluis.tcc.escritorioadvocacia.model.AgendamentoAdvocaticio;
import br.com.joaoluis.tcc.escritorioadvocacia.model.TipoCompromisso;
import br.com.joaoluis.tcc.usuario.model.Usuario;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDateTime;

@Setter
@Getter
@SuperBuilder
public class AgendamentoAdvocaticioIn extends AgendamentoIn implements Serializable {

    private String numeroProcesso;
    private TipoCompromisso tipoCompromisso;
    private String comarca;

    public AgendamentoAdvocaticioIn() {}

    public AgendamentoAdvocaticio toEntity() {
        return AgendamentoAdvocaticio.builder()
                .cliente(Cliente.builder().id(this.getCliente().getId()).build())
                .especialista(Usuario.builder().id(this.getEspecialista().getId()).build())
                .dataInicial(this.getDataInicial())
                .dataFinal(this.getDataFinal())
                .situacao(SituacaoAgendamento.ATIVO)
                .observacao(this.getObservacoes())
                .numeroProcesso(this.numeroProcesso)
                .comarca(this.comarca)
                .tipoCompromisso(this.tipoCompromisso)
                .build();
    }

    public void merge(AgendamentoAdvocaticio agendamento) {
        agendamento.setDataInicial(this.getDataInicial());
        agendamento.setDataFinal(this.getDataFinal());
        agendamento.setCliente(Cliente.builder().id(this.getCliente().getId()).build());
        agendamento.setEspecialista(Usuario.builder().id(this.getEspecialista().getId()).build());
        agendamento.setComarca(this.comarca);
        agendamento.setNumeroProcesso(this.numeroProcesso);
        agendamento.setTipoCompromisso(this.tipoCompromisso);
    }
}
