package br.com.joaoluis.tcc.agendamento.dto;

import br.com.joaoluis.tcc.agendamento.model.Agendamento;
import br.com.joaoluis.tcc.agendamento.model.SituacaoAgendamento;
import br.com.joaoluis.tcc.common.model.Cliente;
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
public class AgendamentoIn implements Serializable {

    private long id;
    @Valid
    @NotNull(message = "O cliente é obrigatório")
    private ClienteId cliente;
    @Valid
    @NotNull(message = "O especialista é obrigatório")
    private EspecialistaId especialista;
    private String observacoes;
    @NotNull(message = "A data inicial é obrigatória")
    private LocalDateTime dataInicial;
    @NotNull(message = "A data final é obrigatória")
    private LocalDateTime dataFinal;

    public AgendamentoIn() {}

    public Agendamento toEntity() {
        return Agendamento.builder()
                .cliente(Cliente.builder().id(this.cliente.getId()).build())
                .especialista(Usuario.builder().id(this.especialista.getId()).build())
                .dataInicial(this.dataInicial)
                .dataFinal(this.dataFinal)
                .situacao(SituacaoAgendamento.ATIVO)
                .observacao(this.observacoes)
                .build();
    }

    public void merge(Agendamento agendamento) {
        agendamento.setDataInicial(this.dataInicial);
        agendamento.setDataFinal(this.dataFinal);
        agendamento.setCliente(Cliente.builder().id(this.cliente.getId()).build());
        agendamento.setEspecialista(Usuario.builder().id(this.especialista.getId()).build());
    }
}
