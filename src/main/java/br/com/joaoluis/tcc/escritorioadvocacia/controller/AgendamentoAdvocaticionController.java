package br.com.joaoluis.tcc.escritorioadvocacia.controller;

import br.com.joaoluis.tcc.agendamento.repository.filter.AgendamentoFilter;
import br.com.joaoluis.tcc.common.dto.EntityId;
import br.com.joaoluis.tcc.escritorioadvocacia.dto.AgendamentoAdvocaticioIn;
import br.com.joaoluis.tcc.escritorioadvocacia.dto.ConsultaAdvocaticiaDto;
import br.com.joaoluis.tcc.escritorioadvocacia.service.AgendamentoAdvocaticioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/agendamentos-advocaticios")
public class AgendamentoAdvocaticionController {

    private final AgendamentoAdvocaticioService service;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public EntityId save(@RequestBody @Valid AgendamentoAdvocaticioIn agendamento) {
        return this.service.save(agendamento);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    public EntityId update(@PathVariable("id") long id, @RequestBody @Valid AgendamentoAdvocaticioIn agendamento) {
        return this.service.update(id, agendamento);
    }

    @GetMapping("/{id}")
    public ConsultaAdvocaticiaDto findById(@PathVariable("id") Long id) {
        return this.service.findById(id);
    }

    @GetMapping("/dashboard")
    public List<ConsultaAdvocaticiaDto> findToDashboard(AgendamentoFilter filter) {
        return this.service.findToDashboard(filter);
    }

    @GetMapping("/consultas-do-dia")
    public List<ConsultaAdvocaticiaDto> findToConsulta() {
        return this.service.findToConsulta();
    }
}
