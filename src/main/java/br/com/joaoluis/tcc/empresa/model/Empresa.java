package br.com.joaoluis.tcc.empresa.model;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Empresa {

    @Id
    @SequenceGenerator(sequenceName="empresa_id_seq", name="empresa_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "empresa_id_seq")
    private Long id;

    @Column(length = 200, nullable = false)
    private String nome;

    @Column(name = "cpf_cnpj", length = 14, nullable = false, unique = true)
    private String cnpjCpf;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_pessoa", length = 20, nullable = false)
    private TipoPessoa tipoPessoa;

    @Column(name = "celular", length = 15)
    private String celular;

    @Column(name = "telefone", length = 15)
    private String telefone;

    @Column(name = "email", columnDefinition = "text", nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "ramo_atividade", length = 30, nullable = false)
    private RamoAtividade ramoAtividade;

    public String getCnpjCpf() {
        return this.cnpjCpf != null ? this.cnpjCpf.replaceAll("[^0-9]", "") : null;
    }
}
