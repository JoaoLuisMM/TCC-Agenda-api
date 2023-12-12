package br.com.joaoluis.tcc.escritorioadvocacia.model;

import br.com.joaoluis.tcc.agendamento.model.Agendamento;
import br.com.joaoluis.tcc.agendamento.model.SituacaoAgendamento;
import br.com.joaoluis.tcc.common.model.Cliente;
import br.com.joaoluis.tcc.empresa.model.Empresa;
import br.com.joaoluis.tcc.usuario.model.Usuario;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@SuperBuilder
@ToString
@Entity
public class AgendamentoAdvocaticio extends Agendamento {

    @Column(length = 100)
    private String numeroProcesso;
    @Enumerated(EnumType.STRING)
    @Column(length = 25, nullable = false)
    private TipoCompromisso tipoCompromisso;
    @Column(length = 150)
    private String comarca;

//    @Builder(builderMethodName = "agendamentoAdvocaticioBuilder")
//    public AgendamentoAdvocaticio(Long id, String observacao,
//                                  LocalDateTime dataInicial, LocalDateTime dataFinal, Cliente cliente,
//                                  Usuario especialista, Empresa empresa, String numeroProcesso,
//                                  TipoCompromisso tipoCompromisso, String comarca) {
//        super(id, observacao, dataInicial, dataFinal, cliente, especialista, empresa);
//        this.numeroProcesso = numeroProcesso;
//        this.tipoCompromisso = tipoCompromisso;
//        this.comarca = comarca;
//    }
}
