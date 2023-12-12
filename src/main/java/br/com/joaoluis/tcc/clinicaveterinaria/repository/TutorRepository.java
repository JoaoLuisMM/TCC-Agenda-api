package br.com.joaoluis.tcc.clinicaveterinaria.repository;

import br.com.joaoluis.tcc.clinicaveterinaria.model.Tutor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TutorRepository extends JpaRepository<Tutor, Long> {
    Optional<Tutor> findByCpf(String cpf);
}
