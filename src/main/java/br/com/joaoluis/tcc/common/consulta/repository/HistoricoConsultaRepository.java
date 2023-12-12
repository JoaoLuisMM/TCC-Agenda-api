package br.com.joaoluis.tcc.common.consulta.repository;

import br.com.joaoluis.tcc.common.consulta.model.HistoricoConsulta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoricoConsultaRepository extends JpaRepository<HistoricoConsulta, Long> {
    List<HistoricoConsulta> findAllByClienteIdOrderByIdDesc(long clienteId);
}
