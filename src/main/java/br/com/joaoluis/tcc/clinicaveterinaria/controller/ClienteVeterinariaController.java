package br.com.joaoluis.tcc.clinicaveterinaria.controller;

import br.com.joaoluis.tcc.clinicaveterinaria.dto.ClienteVeterinariaIn;
import br.com.joaoluis.tcc.clinicaveterinaria.dto.ClienteVeterinariaList;
import br.com.joaoluis.tcc.clinicaveterinaria.dto.ClienteVeterinariaOut;
import br.com.joaoluis.tcc.clinicaveterinaria.model.ClienteVeterinaria;
import br.com.joaoluis.tcc.clinicaveterinaria.repository.filter.ClienteVeterinariaFilter;
import br.com.joaoluis.tcc.clinicaveterinaria.service.ClienteVeterinariaService;
import br.com.joaoluis.tcc.common.dto.ClienteAgendamento;
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
@RequestMapping("/api/clinica-veterinaria/clientes")
public class ClienteVeterinariaController {

    private final ClienteVeterinariaService service;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public EntityId save(@RequestBody @Valid ClienteVeterinariaIn cliente) {
        return this.service.save(cliente);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable("id") Long id, @RequestBody @Valid ClienteVeterinariaIn cliente) {
        this.service.update(id, cliente);
    }

    @GetMapping("/{id}")
    public ClienteVeterinariaOut findById(@PathVariable("id") Long clienteId) {
        final ClienteVeterinaria cliente = this.service.findById(clienteId);
        return ClienteVeterinariaOut.of(cliente);
    }

    @GetMapping
    public Page<ClienteVeterinariaList> findAll(ClienteVeterinariaFilter filter, Pageable pageable) {
        return this.service.findAll(filter, pageable);
    }

    @GetMapping("/to-agendamento")
    public List<ClienteAgendamento> findClientesAgendamento() {
        return this.service.findClientesAgendamento().stream()
                .map(c -> new ClienteAgendamento(c.getId(), c.getCpf(), c.getNome())).toList();
    }
}
