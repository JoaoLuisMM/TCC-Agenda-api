package br.com.joaoluis.tcc.escritorioadvocacia.service;

import br.com.joaoluis.tcc.agendamento.repository.filter.AgendamentoFilter;
import br.com.joaoluis.tcc.agendamento.validation.AgendamentoValidation;
import br.com.joaoluis.tcc.common.dto.EntityId;
import br.com.joaoluis.tcc.common.exception.ResourceNotFoundException;
import br.com.joaoluis.tcc.escritorioadvocacia.dto.AgendamentoAdvocaticioIn;
import br.com.joaoluis.tcc.escritorioadvocacia.dto.ConsultaAdvocaticiaDto;
import br.com.joaoluis.tcc.escritorioadvocacia.model.AgendamentoAdvocaticio;
import br.com.joaoluis.tcc.escritorioadvocacia.repository.AgendamentoAdvocaticioRepository;
import br.com.joaoluis.tcc.seguranca.service.AuthenticationService;
import br.com.joaoluis.tcc.usuario.model.Usuario;
import br.com.joaoluis.tcc.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

// Faz a injeção de dependências a partir do construtor para os atributos "marcados" como "final".
@RequiredArgsConstructor
// Configura a classe AgendamentoService como um componente do Spring, que passa a gerenciá-la.
@Service
public class AgendamentoAdvocaticioService {

    private static final String MSG_AGENDAMENTO_NAO_ENCOTRADO = "Agendamento com ID %d não encontrado";

    private final AuthenticationService authenticationService;
    private final AgendamentoAdvocaticioRepository agendamentoRepository;
    private final AgendamentoValidation validation;
    private final UsuarioRepository usuarioRepository;

    public EntityId save(AgendamentoAdvocaticioIn agendamento) {
        this.validation.validate(agendamento);
        final AgendamentoAdvocaticio novoAgendamento = agendamento.toEntity();
        final Usuario usuario = this.authenticationService.getUsuario();
        novoAgendamento.setEmpresa(usuario.getEmpresa());
        this.agendamentoRepository.save(novoAgendamento);
        return new EntityId(novoAgendamento.getId());
    }

    public EntityId update(long id, AgendamentoAdvocaticioIn agendamentoIn) {
        final AgendamentoAdvocaticio agendamento = this.agendamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(MSG_AGENDAMENTO_NAO_ENCOTRADO, id)));
        this.validation.validate(id, agendamentoIn);
        agendamentoIn.merge(agendamento);
        this.agendamentoRepository.save(agendamento);
        return new EntityId(id);
    }

    public ConsultaAdvocaticiaDto findById(Long id) {
        return this.agendamentoRepository.findById(id)
                .map(ConsultaAdvocaticiaDto::of)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(MSG_AGENDAMENTO_NAO_ENCOTRADO, id)));
    }

    public List<ConsultaAdvocaticiaDto> findToDashboard(AgendamentoFilter<AgendamentoAdvocaticio> filter) {
        final Usuario usuario = this.authenticationService.getUsuario();
        filter.setEmpresaId(usuario.getEmpresaId());
        // Recupera uma página de registros de agendamentos
        final List<AgendamentoAdvocaticio> agendamentos = this.agendamentoRepository.findAll(filter.getRestricoes(), Sort.by("dataInicial"));
        // Transforma os objetos "agendamento" em objetos "agendamentoDto" e retorna uma página desses dto´s
        return agendamentos.stream().map(ConsultaAdvocaticiaDto::of).toList();
    }

    public List<ConsultaAdvocaticiaDto> findToConsulta() {
        AgendamentoFilter filter = new AgendamentoFilter<AgendamentoAdvocaticio>();
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
        final List<AgendamentoAdvocaticio> agendamentos = this.agendamentoRepository.findAll(filter.getRestricoes(), Sort.by("dataInicial"));
        // Transforma os objetos "agendamento" em objetos "agendamentoDto" e retorna uma página desses dto´s
        return agendamentos.stream().map(ConsultaAdvocaticiaDto::of).toList();
    }
}
