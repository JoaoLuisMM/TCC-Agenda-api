package br.com.joaoluis.tcc.common.consulta.controller;

import br.com.joaoluis.tcc.common.consulta.dto.HistoricoConsultaOut;
import br.com.joaoluis.tcc.common.consulta.dto.HistoricoConsultaIn;
import br.com.joaoluis.tcc.common.consulta.service.HistoricoConsultaService;
import br.com.joaoluis.tcc.common.dto.EntityId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/clientes")
public class ConsultaController {

    private final HistoricoConsultaService consultaService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{id}/consultas")
    public EntityId save(@PathVariable("id") long clienteId, @RequestBody @Valid HistoricoConsultaIn consulta) {
        return this.consultaService.save(clienteId, consulta);
    }

    // TODO implementar a edição de uma consulta??
//    @ResponseStatus(HttpStatus.OK)
//    @PutMapping("/{id}/consultas/{consultaId}")
//    public void update(@PathVariable("consultaId") long id, @RequestBody @Valid HistoricoConsultaIn agendamento) {
//        this.clienteAgendamentoService.update(id, agendamento);
//    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}/consultas")
    public HistoricoConsultaOut findClienteByid(@PathVariable("id") long clienteId) {
        return this.consultaService.findHistoricoByClienteId(clienteId);
    }
}
