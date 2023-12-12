package br.com.joaoluis.tcc.common.consulta.dto;

import br.com.joaoluis.tcc.common.consulta.model.HistoricoConsulta;
import jakarta.validation.constraints.NotBlank;

public record HistoricoConsultaIn(@NotBlank(message = "A observação é obrigatória") String observacoes) {
    public HistoricoConsulta toEntity() {
        return HistoricoConsulta.builder()
                .observacao(observacoes)
                .build();
    }
}
