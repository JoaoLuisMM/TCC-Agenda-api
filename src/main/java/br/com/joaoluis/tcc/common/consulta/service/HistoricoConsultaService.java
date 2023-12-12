package br.com.joaoluis.tcc.common.consulta.service;

import br.com.joaoluis.tcc.common.consulta.dto.HistoricoConsultaOut;
import br.com.joaoluis.tcc.common.consulta.dto.HistoricoConsultaIn;
import br.com.joaoluis.tcc.common.consulta.model.HistoricoConsulta;
import br.com.joaoluis.tcc.common.consulta.repository.HistoricoConsultaRepository;
import br.com.joaoluis.tcc.common.dto.EntityId;
import br.com.joaoluis.tcc.common.exception.ResourceNotFoundException;
import br.com.joaoluis.tcc.common.model.Cliente;
import br.com.joaoluis.tcc.common.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

// Faz a injeção de dependências a partir do construtor para os atributos "marcados" como "final".
@RequiredArgsConstructor
// Configura a classe AgendamentoService como um componente do Spring, que passa a gerenciá-la.
@Service
public class HistoricoConsultaService {

    private final ClienteRepository clienteRepository;
    private final HistoricoConsultaRepository repository;

    public EntityId save(long clienteId, HistoricoConsultaIn consultaIn) {
        if (!this.clienteRepository.existsById(clienteId)) {
            throw new ResourceNotFoundException(String.format("Cliente com ID %d não encontrado", clienteId));
        }
        HistoricoConsulta consulta = consultaIn.toEntity();
        consulta.setCliente(Cliente.builder().id(clienteId).build());
        return new EntityId(this.repository.save(consulta).getId());
    }

    public HistoricoConsultaOut findHistoricoByClienteId(long clienteId) {
        final Cliente cliente = this.clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Cliente com ID %d não encontrado", clienteId)));
        final List<HistoricoConsulta> historico = this.repository.findAllByClienteIdOrderByIdDesc(clienteId);
        final Long ultimaConsultaId = historico.stream().filter(h -> h.getDataCadastro().equals(LocalDate.now()))
                .map(HistoricoConsulta::getId).findFirst().orElse(null);
        return new HistoricoConsultaOut(clienteId, cliente.getNome(), ultimaConsultaId, historico.stream().map(br.com.joaoluis.tcc.common.consulta.model.HistoricoConsulta::getObservacao).toList());
    }
}
