package br.com.joaoluis.tcc.agendamento.service;

import br.com.joaoluis.tcc.agendamento.repository.EspecialistaRepository;
import br.com.joaoluis.tcc.agendamento.repository.filter.EspecialistaFilter;
import br.com.joaoluis.tcc.seguranca.service.AuthenticationService;
import br.com.joaoluis.tcc.usuario.model.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class EspecialistaService {

    private final AuthenticationService authenticationService;
    private final EspecialistaRepository repository;

    public List<Usuario> findEspecialistasAgendamento() {
        long empresaId = authenticationService.getEmpresaId();
        EspecialistaFilter filter = new EspecialistaFilter();
        return this.repository.findAll(filter.getRestricoes(empresaId));
    }
}
