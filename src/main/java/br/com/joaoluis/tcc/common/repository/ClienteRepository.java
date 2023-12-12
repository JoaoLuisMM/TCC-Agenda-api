package br.com.joaoluis.tcc.common.repository;

import br.com.joaoluis.tcc.common.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long>, JpaSpecificationExecutor<Cliente> {
    List<Cliente> findAllByCpf(String cpf);

    boolean existsByCpf(String cpf);

    boolean existsByCpfAndEmpresaId(String cpf, long empresaId);
}
