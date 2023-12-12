package br.com.joaoluis.tcc.agendamento.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClienteId implements Serializable {

    @NotNull(message = "O cliente é obrigatório")
    private Long id;
}
