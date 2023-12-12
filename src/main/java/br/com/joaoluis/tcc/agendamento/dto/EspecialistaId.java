package br.com.joaoluis.tcc.agendamento.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EspecialistaId implements Serializable {

    @NotNull(message = "O especialista é obrigatório")
    private Long id;
}
