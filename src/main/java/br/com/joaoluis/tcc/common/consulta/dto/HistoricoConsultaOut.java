package br.com.joaoluis.tcc.common.consulta.dto;

import java.util.List;

public record HistoricoConsultaOut(
    Long clienteId,
    String nome,
    Long ultimaConsultaId,
    List<String> historico
) {
}
