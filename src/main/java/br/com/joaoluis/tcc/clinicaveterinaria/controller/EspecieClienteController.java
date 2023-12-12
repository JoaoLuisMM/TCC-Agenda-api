package br.com.joaoluis.tcc.clinicaveterinaria.controller;

import br.com.joaoluis.tcc.clinicaveterinaria.model.EspecieCliente;
import br.com.joaoluis.tcc.clinicaveterinaria.repository.EspecieClienteRepository;
import br.com.joaoluis.tcc.common.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/clinica-veterinaria/especies")
public class EspecieClienteController {

    private final EspecieClienteRepository repository;

    @PostMapping
    public EspecieCliente save(@RequestBody @Valid EspecieCliente cliente) {
        return this.repository.save(cliente);
    }

    @PutMapping
    public EspecieCliente update(@PathVariable("id") Long id, @RequestBody @Valid EspecieCliente especie) {
        final EspecieCliente especieCliente = this.getById(id);
        especieCliente.setDescricao(especie.getDescricao());
        return this.repository.save(especieCliente);
    }

    @GetMapping("/{id}")
    public EspecieCliente findById(@PathVariable("id") Long clienteId) {
        return this.repository.findById(clienteId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("A espécie com ID %d não foi encontrado", clienteId)));
    }

    @GetMapping
    public List<EspecieCliente> findAll() {
        return this.repository.findAll(Sort.by("descricao"));
    }

    public EspecieCliente getById(Long especieId) {
        return this.repository.findById(especieId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("A espécie com ID %d não foi encontrado", especieId)));
    }
}
