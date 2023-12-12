package br.com.joaoluis.tcc.clinicaveterinaria.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "especie_cliente_veterinaria")
public class EspecieCliente {

    @Id
    @SequenceGenerator(sequenceName="especie_cliente_id_seq", name="especie_cliente_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "especie_cliente_id_seq")
    private Long id;

    @NotBlank(message = "A descrição é obrigatória")
    @Size(max = 200, message = "A descrição deve ter no máximo 200 caracteres")
    @Column(name = "descricao", length = 200, nullable = false, unique = true)
    private String descricao;

}
