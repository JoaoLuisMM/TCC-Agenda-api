package br.com.joaoluis.tcc.common.dto;

import jakarta.validation.constraints.NotNull;

public record UsuarioId(@NotNull(message = "O usuário é obrigatório") Long id) {
}
