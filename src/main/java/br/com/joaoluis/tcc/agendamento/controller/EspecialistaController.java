package br.com.joaoluis.tcc.agendamento.controller;

import br.com.joaoluis.tcc.agendamento.dto.EspecialistaAgendamento;
import br.com.joaoluis.tcc.agendamento.service.EspecialistaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/especialistas")
public class EspecialistaController {

    private final EspecialistaService service;

    @GetMapping("/to-agendamento")
    public List<EspecialistaAgendamento> findEspecialistasAgendamento() {
        return this.service.findEspecialistasAgendamento().stream()
                .map(c -> new EspecialistaAgendamento(c.getId(), c.getCpf(), c.getNome())).toList();
    }
}
