package br.com.joaoluis.tcc.escritorioadvocacia.repository;

import br.com.joaoluis.tcc.escritorioadvocacia.model.AgendamentoAdvocaticio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AgendamentoAdvocaticioRepository extends JpaRepository<AgendamentoAdvocaticio, Long>, JpaSpecificationExecutor<AgendamentoAdvocaticio> {
}
