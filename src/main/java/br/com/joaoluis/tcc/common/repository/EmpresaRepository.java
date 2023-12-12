package br.com.joaoluis.tcc.common.repository;

import br.com.joaoluis.tcc.empresa.model.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
    boolean existsByCnpjCpf(String cnpjCpf);
}
