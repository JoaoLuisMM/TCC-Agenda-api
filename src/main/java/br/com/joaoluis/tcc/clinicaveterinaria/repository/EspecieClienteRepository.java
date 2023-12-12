package br.com.joaoluis.tcc.clinicaveterinaria.repository;

import br.com.joaoluis.tcc.clinicaveterinaria.model.EspecieCliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EspecieClienteRepository extends JpaRepository<EspecieCliente, Long> {
}
