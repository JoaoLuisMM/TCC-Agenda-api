package br.com.joaoluis.tcc.clinicaveterinaria.model;

import br.com.joaoluis.tcc.empresa.model.Empresa;
import br.com.joaoluis.tcc.common.model.Cliente;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@ToString
@Entity
public class ClienteVeterinaria extends Cliente {

    @Column(name = "raca", length = 30)
    private String raca;

    @Enumerated(EnumType.STRING)
    @Column(name = "sexo", length = 10)
    private Sexo sexo;

    @ManyToOne
    @JoinColumn(name = "especie_id", referencedColumnName = "id", nullable = false)
    private EspecieCliente especie;

    @Valid
    @ManyToOne
    @JoinColumn(name = "tutor_id", referencedColumnName = "id", nullable = false)
    private Tutor tutor;

    @Builder(builderMethodName = "clienteVeterinariaBuilder")
    public ClienteVeterinaria(Long id, Empresa empresa, String cpf, String nome, String email,
                              String celular, String rua, String numero, String complemento,
                              String cep, String bairro, String cidade, Boolean inativo,
                              EspecieCliente especie, Tutor tutor, Sexo sexo, LocalDate dataNascimento,
                              String raca) {
        super(id, empresa, cpf, nome, email, celular, rua, numero, complemento, cep, bairro, cidade, inativo, dataNascimento);
        this.especie = especie;
        this.tutor = tutor;
        this.raca = raca;
        this.sexo = sexo;
    }
}
