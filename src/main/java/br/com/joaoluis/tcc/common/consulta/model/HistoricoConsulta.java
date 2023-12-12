package br.com.joaoluis.tcc.common.consulta.model;

import br.com.joaoluis.tcc.common.model.Cliente;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class HistoricoConsulta {

    @Id
    @SequenceGenerator(sequenceName="historico_consulta_id_seq", name="historico_consulta_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "historico_consulta_id_seq")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Column(name = "observacao", columnDefinition = "text", nullable = false)
    private String observacao;

    @Column(name = "data_cadastro", nullable = false)
    private LocalDate dataCadastro;

    @PrePersist
    public void createdDate() {
        this.dataCadastro = LocalDate.now();
    }
}
