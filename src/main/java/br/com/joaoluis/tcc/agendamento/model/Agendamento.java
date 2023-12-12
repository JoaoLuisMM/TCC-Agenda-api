package br.com.joaoluis.tcc.agendamento.model;

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
@AllArgsConstructor
@SuperBuilder
@ToString
@Inheritance(strategy = InheritanceType.JOINED)
@Entity
public class Agendamento {

    @Id
    @SequenceGenerator(sequenceName="agendamento_id_seq", name="agendamento_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "agendamento_id_seq")
    private Long id;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "situacao", length = 25)
    private SituacaoAgendamento situacao = SituacaoAgendamento.ATIVO;

    @Column(name = "observacao", columnDefinition = "text")
    private String observacao;

    @Column(nullable = false)
    private LocalDateTime dataInicial;
    private LocalDateTime dataFinal;

    @ManyToOne
    @JoinColumn(name = "cliente_id", referencedColumnName = "id", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "especialista_id", referencedColumnName = "id", nullable = false)
    private Usuario especialista;

    @ManyToOne
    @JoinColumn(name = "empresa_id", referencedColumnName = "id", nullable = false)
    private Empresa empresa;

    public Agendamento(Long id, String observacao, LocalDateTime dataInicial, LocalDateTime dataFinal,
                       Cliente cliente, Usuario especialista, Empresa empresa) {
        this.id = id;
        this.situacao = SituacaoAgendamento.ATIVO;
        this.observacao = observacao;
        this.dataInicial = dataInicial;
        this.dataFinal = dataFinal;
        this.cliente = cliente;
        this.especialista = especialista;
        this.empresa = empresa;
    }
}
