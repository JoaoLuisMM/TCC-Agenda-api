package br.com.joaoluis.tcc.clinicaveterinaria.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Tutor {

    @Id
    @SequenceGenerator(sequenceName="tutor_id_seq", name="tutor_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tutor_id_seq")
    private Long id;

    @Column(name = "cpf", length = 11, nullable = false, unique = true)
    private String cpf;

    @NotBlank(message = "O nome é obrigatório")
    @Column(name = "nome", length = 250, nullable = false)
    protected String nome;
    @Column(name = "email", columnDefinition = "text", unique = true)
    private String email;
    @Column(name = "celular", length = 11)
    private String celular;

    @Column(name = "data_cadastro", nullable = false)
    protected LocalDateTime dataCadastro;

    public String getCpf() {
        return this.cpf != null ? this.cpf.replaceAll("[^0-9]", "") : null;
    }

    public String getCelular() {
        return celular != null ? this.celular.replaceAll("[^0-9]", "") : null;
    }

    @PrePersist
    public void createdDate() {
        this.dataCadastro = LocalDateTime.now();
    }

}
