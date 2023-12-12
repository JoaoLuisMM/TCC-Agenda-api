package br.com.joaoluis.tcc.agendamento.controller;

import br.com.joaoluis.tcc.agendamento.dto.AgendamentoDto;
import br.com.joaoluis.tcc.agendamento.dto.AgendamentoIn;
import br.com.joaoluis.tcc.agendamento.dto.ConsultaVeterinariaDto;
import br.com.joaoluis.tcc.agendamento.repository.filter.AgendamentoFilter;
import br.com.joaoluis.tcc.agendamento.service.AgendamentoService;
import br.com.joaoluis.tcc.common.dto.EntityId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/agendamentos")
public class AgendamentoController {

    private final AgendamentoService service;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public EntityId save(@RequestBody @Valid AgendamentoIn agendamento) {
        return this.service.save(agendamento);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    public EntityId update(@PathVariable("id") long id, @RequestBody @Valid AgendamentoIn agendamento) {
        return this.service.update(id, agendamento);
    }

    @GetMapping("/{id}")
    public AgendamentoDto findById(@PathVariable("id") Long id) {
        return this.service.findById(id);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") Long id) {
        this.service.deleteById(id);
    }

    @GetMapping("/dashboard")
    public List<AgendamentoDto> findToDashboard(AgendamentoFilter filter) {
        return this.service.findToDashboard(filter);
    }

    @GetMapping("/consultas-do-dia")
    public List<AgendamentoDto> findToConsulta() {
        return this.service.findToConsulta();
    }

    @GetMapping("/veterinaria/consultas-do-dia")
    public List<ConsultaVeterinariaDto> findToConsultaVeterinaria() {
        return this.service.findToConsultaVeterinaria();
    }

    @GetMapping
    public Page<AgendamentoDto> findAll(AgendamentoFilter filter, Pageable pageable) {
        return this.service.findAll(filter, pageable);
    }
}
