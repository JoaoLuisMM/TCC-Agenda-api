package br.com.joaoluis.tcc.agendamento.service;

import br.com.joaoluis.tcc.agendamento.dto.AgendamentoDto;
import br.com.joaoluis.tcc.agendamento.dto.AgendamentoIn;
import br.com.joaoluis.tcc.agendamento.dto.ConsultaVeterinariaDto;
import br.com.joaoluis.tcc.agendamento.model.Agendamento;
import br.com.joaoluis.tcc.agendamento.model.SituacaoAgendamento;
import br.com.joaoluis.tcc.agendamento.repository.AgendamentoRepository;
import br.com.joaoluis.tcc.agendamento.repository.filter.AgendamentoFilter;
import br.com.joaoluis.tcc.agendamento.validation.AgendamentoValidation;
import br.com.joaoluis.tcc.common.dto.EntityId;
import br.com.joaoluis.tcc.common.exception.ResourceNotFoundException;
import br.com.joaoluis.tcc.seguranca.service.AuthenticationService;
import br.com.joaoluis.tcc.usuario.model.Usuario;
import br.com.joaoluis.tcc.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

// Faz a injeção de dependências a partir do construtor para os atributos "marcados" como "final".
@RequiredArgsConstructor
// Configura a classe AgendamentoService como um componente do Spring, que passa a gerenciá-la.
@Service
public class AgendamentoService {

    private static final String MSG_AGENDAMENTO_NAO_ENCOTRADO = "Agendamento com ID %d não encontrado";

    private final AuthenticationService authenticationService;
    private final AgendamentoRepository agendamentoRepository;
    private final AgendamentoValidation validation;
    private final UsuarioRepository usuarioRepository;

    public EntityId save(AgendamentoIn agendamento) {
        this.validation.validate(agendamento);
        final Agendamento novoAgendamento = agendamento.toEntity();
        final Usuario usuario = this.authenticationService.getUsuario();
        novoAgendamento.setEmpresa(usuario.getEmpresa());
        this.agendamentoRepository.save(novoAgendamento);
        return new EntityId(novoAgendamento.getId());
    }

    public EntityId update(long id, AgendamentoIn agendamentoIn) {
        final Agendamento agendamento = this.agendamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(MSG_AGENDAMENTO_NAO_ENCOTRADO, id)));
        this.validation.validate(id, agendamentoIn);
        agendamentoIn.merge(agendamento);
        this.agendamentoRepository.save(agendamento);
        return new EntityId(id);
    }

    public void deleteById(Long id) {
        final Agendamento agendamento = this.agendamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(MSG_AGENDAMENTO_NAO_ENCOTRADO, id)));
        agendamento.setSituacao(SituacaoAgendamento.INATIVO);
        this.agendamentoRepository.save(agendamento);
    }

    public Page<AgendamentoDto> findAll(AgendamentoFilter filter, Pageable pageable) {
        final Usuario usuario = this.authenticationService.getUsuario();
        filter.setEmpresaId(usuario.getEmpresaId());
        // Recupera uma página de registros de agendamentos
        final Page<Agendamento> agendamentos = this.agendamentoRepository.findAll(filter.getRestricoes(), pageable);
        // Transforma os objetos "agendamento" em objetos "agendamentoDto" e retorna uma página desses dto´s
        return agendamentos.map(AgendamentoDto::of);
    }

    public List<AgendamentoDto> findToDashboard(AgendamentoFilter<Agendamento> filter) {
        final Usuario usuario = this.authenticationService.getUsuario();
//        if (usuario.isOperacional()) {
//            final Especialista especialista = this.especialistaRepository.findByUsuarioId(usuario.getId())
//                    .orElseThrow(() -> new ResourceNotFoundException("Responsável pelos agendamentos não encontrado"));
//            filter.setEspecialistaId(especialista.getId());
//            LocalDateTime dataAtual = LocalDateTime.now();
//            filter.setDataInicial(dataAtual.withHour(8).withMinute(0).withSecond(0));
//            filter.setDataFinal(dataAtual.withHour(18).withMinute(0).withSecond(0));
//        }
        filter.setEmpresaId(usuario.getEmpresaId());
        // Recupera uma página de registros de agendamentos
        final List<Agendamento> agendamentos = this.agendamentoRepository.findAll(filter.getRestricoes(), Sort.by("dataInicial"));
        // Transforma os objetos "agendamento" em objetos "agendamentoDto" e retorna uma página desses dto´s
        return agendamentos.stream().map(AgendamentoDto::of).toList();
    }

    public List<AgendamentoDto> findToConsulta() {
        AgendamentoFilter filter = new AgendamentoFilter<Agendamento>();
        final Usuario usuario = this.authenticationService.getUsuario();
        if (usuario.isOperacional()) {
            final Usuario especialista = this.usuarioRepository.findById(usuario.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Responsável pelos agendamentos não encontrado"));
            filter.setEspecialistaId(especialista.getId());
            LocalDateTime dataAtual = LocalDateTime.now();
            filter.setDataInicial(dataAtual.withHour(8).withMinute(0).withSecond(0));
            filter.setDataFinal(dataAtual.withHour(18).withMinute(0).withSecond(0));
        }
        filter.setEmpresaId(usuario.getEmpresaId());
        // Recupera uma página de registros de agendamentos
        final List<Agendamento> agendamentos = this.agendamentoRepository.findAll(filter.getRestricoes(), Sort.by("dataInicial"));
        // Transforma os objetos "agendamento" em objetos "agendamentoDto" e retorna uma página desses dto´s
        return agendamentos.stream().map(AgendamentoDto::of).toList();
    }

    // TODO criar um dto para as consultas em clínicas veterinárias
    public List<ConsultaVeterinariaDto> findToConsultaVeterinaria() {
        AgendamentoFilter filter = new AgendamentoFilter();
        final Usuario usuario = this.authenticationService.getUsuario();
        if (usuario.isOperacional()) {
            final Usuario especialista = this.usuarioRepository.findById(usuario.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Responsável pelos agendamentos não encontrado"));
            filter.setEspecialistaId(especialista.getId());
            LocalDateTime dataAtual = LocalDateTime.now();
            filter.setDataInicial(dataAtual.withHour(8).withMinute(0).withSecond(0));
            filter.setDataFinal(dataAtual.withHour(18).withMinute(0).withSecond(0));
        }
        filter.setEmpresaId(usuario.getEmpresaId());
        // Recupera uma página de registros de agendamentos
        final List<Agendamento> agendamentos = this.agendamentoRepository.findAll(filter.getRestricoes(), Sort.by("dataInicial"));
        // Transforma os objetos "agendamento" em objetos "agendamentoDto" e retorna uma página desses dto´s
        return agendamentos.stream().map(ConsultaVeterinariaDto::of).toList();
    }

    public AgendamentoDto findById(Long id) {
        return this.agendamentoRepository.findById(id)
                .map(AgendamentoDto::of)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(MSG_AGENDAMENTO_NAO_ENCOTRADO, id)));
    }
}
