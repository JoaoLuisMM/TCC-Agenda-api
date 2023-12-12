package br.com.joaoluis.tcc.clinicaveterinaria.repository;

import br.com.joaoluis.tcc.clinicaveterinaria.model.ClienteVeterinaria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ClienteVeterinariaRepository extends JpaRepository<ClienteVeterinaria, Long>, JpaSpecificationExecutor<ClienteVeterinaria> {
    List<ClienteVeterinaria> findByTutorId(long tutorId);
}
