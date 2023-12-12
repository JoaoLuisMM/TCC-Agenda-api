package br.com.joaoluis.tcc.common.controller;

import br.com.joaoluis.tcc.common.dto.*;
import br.com.joaoluis.tcc.common.model.Cliente;
import br.com.joaoluis.tcc.common.repository.filter.ClienteFilter;
import br.com.joaoluis.tcc.common.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService service;

    @PostMapping
    public EntityId save(@RequestBody @Valid ClienteIn cliente) {
        return this.service.save(cliente);
    }

    @PutMapping("/{id}")
    public EntityId update(@PathVariable("id") long id, @RequestBody @Valid ClienteIn cliente) {
        return this.service.update(id, cliente);
    }

    @GetMapping("/{id}")
    public ClienteOut findById(@PathVariable("id") long id) {
        return this.service.findById(id);
    }

    @GetMapping
    public Page<ClienteList> findAll(ClienteFilter filter, Pageable pageable) {
        return this.service.findAll(filter, pageable);
    }

    @GetMapping("/to-agendamento")
    public List<ClienteAgendamento> findClientesAgendamento() {
        return this.service.findClientesAgendamento().stream()
                .map(c -> new ClienteAgendamento(c.getId(), c.getCpf(), c.getNome())).collect(Collectors.toList());
    }
}
