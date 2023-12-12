package br.com.joaoluis.tcc.common.controller;

import br.com.joaoluis.tcc.common.dto.*;
import br.com.joaoluis.tcc.common.repository.filter.FuncionarioFilter;
import br.com.joaoluis.tcc.common.service.FuncionarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/funcionarios")
public class FuncionarioController {

    private final FuncionarioService service;

    @PostMapping
    public EntityId save(@RequestBody @Valid FuncionarioIn funcionario) {
        return this.service.save(funcionario);
    }

    @PutMapping("/{id}")
    public EntityId update(@PathVariable("id") long id, @RequestBody @Valid FuncionarioIn funcionario) {
        return this.service.update(id, funcionario);
    }

    @PatchMapping("/{id}/situacao")
    public void updateSituacao(@PathVariable("id") long id) {
        this.service.updateSituacao(id);
    }

    @GetMapping("/{id}")
    public FuncionarioOut findById(@PathVariable("id") long id) {
        return this.service.findById(id);
    }

    @GetMapping
    public Page<FuncionarioOut> findAll(FuncionarioFilter filter, Pageable pageable) {
        return this.service.findAll(filter, pageable);
    }
}
