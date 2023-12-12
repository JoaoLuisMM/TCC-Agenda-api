package br.com.joaoluis.tcc.common.model;

import br.com.joaoluis.tcc.common.consulta.model.HistoricoConsulta;
import br.com.joaoluis.tcc.empresa.model.Empresa;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Inheritance(strategy = InheritanceType.JOINED)
@Entity
public class Cliente {

    @Id
    @SequenceGenerator(sequenceName="paciente_id_seq", name="paciente_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "paciente_id_seq")
    protected Long id;

    @ManyToOne
    @JoinColumn(name = "empresa_id", referencedColumnName = "id", nullable = false)
    protected Empresa empresa;

    @Column(name = "cpf", length = 11, nullable = false) // Deixou de ser único por causa dos clientes da clínica veterinária
    protected String cpf;
    @NotBlank(message = "O nome é obrigatório")
    @Column(name = "nome", length = 250, nullable = false)
    protected String nome;
    @Column(name = "email", columnDefinition = "text")
    protected String email;
    @Column(name = "celular", length = 11)
    protected String celular;
    protected LocalDate dataNascimento;

    @Column(name = "rua", length = 120)
    protected String rua;
    @Column(name = "numero", length = 40)
    protected String numero;
    @Column(name = "complemento", length = 100)
    protected String complemento;
    @Column(name = "cep", length = 8)
    protected String cep;
    @Column(name = "bairro", length = 80)
    protected String bairro;
    @Column(name = "cidade", length = 60)
    protected String cidade;

    @Builder.Default
    @Column(name = "st_inativo")
    protected Boolean inativo = false;

    @Column(name = "data_cadastro", nullable = false)
    protected LocalDateTime dataCadastro;

    @OneToMany(mappedBy = "cliente")
    private List<HistoricoConsulta> historico;

    public Cliente(Long id, Empresa empresa, String cpf, String nome, String email, String celular,
                   String rua, String numero, String complemento, String cep, String bairro,
                   String cidade, Boolean inativo, LocalDate dataNascimento) {
        this.id = id;
        this.empresa = empresa;
        this.cpf = cpf;
        this.nome = nome;
        this.email = email;
        this.celular = celular;
        this.rua = rua;
        this.numero = numero;
        this.complemento = complemento;
        this.cep = cep;
        this.bairro = bairro;
        this.cidade = cidade;
        this.inativo = inativo;
        this.dataNascimento = dataNascimento;
    }

    public String getCpf() {
        return this.cpf != null ? this.cpf.replaceAll("[^0-9]", "") : null;
    }

    public String getCelular() {
        return celular != null ? this.celular.replaceAll("[^0-9]", "") : null;
    }

    public String getCep() {
        return this.cep != null ? this.cep.replaceAll("[^0-9]", "") : null;
    }

    public Boolean getInativo() {
        return this.inativo != null ? this.inativo : false;
    }

    @PrePersist
    public void createdDate() {
        this.dataCadastro = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cliente cliente = (Cliente) o;
        return id.equals(cliente.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
