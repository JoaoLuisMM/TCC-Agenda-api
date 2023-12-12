package br.com.joaoluis.tcc.usuario.controller;

import br.com.joaoluis.tcc.common.error.Error;
import br.com.joaoluis.tcc.usuario.dto.UsuarioEdit;
import br.com.joaoluis.tcc.usuario.dto.UsuarioIn;
import br.com.joaoluis.tcc.usuario.dto.UsuarioOut;
import br.com.joaoluis.tcc.usuario.repository.filter.UsuarioFilter;
import br.com.joaoluis.tcc.usuario.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@CrossOrigin({"http://localhost:4200"})
@RequestMapping("/api/usuarios")
@RestController
public class UsuarioController {

    private final UsuarioService service;

    @PostMapping
    public void save(@RequestBody @Valid UsuarioIn usuario) {
        this.service.save(usuario);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable("id") Long id, @RequestBody @Valid UsuarioEdit usuario) {
        this.service.update(id, usuario);
    }

    @GetMapping
    public ResponseEntity findAll(UsuarioFilter filter, Pageable pageable) {
        if (filter.getNome() != null && filter.getNome().length() < 3) {
            List<Error> erros = Arrays.asList(Error.builder().message("A parte do nome deve possuir pelo menos 3 caracteres").build());
            return ResponseEntity.badRequest().body(erros);
        }
        return ResponseEntity.ok(this.service.findAll(filter, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity findById(@PathVariable("id") Long usuarioId) {
        return ResponseEntity.ok(UsuarioOut.of(this.service.getOne(usuarioId)));
    }
}
