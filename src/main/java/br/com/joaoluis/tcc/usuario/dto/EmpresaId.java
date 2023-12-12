package br.com.joaoluis.tcc.usuario.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaId implements Serializable {
    @NotNull(message = "A empresa é obrigatória")
    private Long id;
}
