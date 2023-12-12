package br.com.joaoluis.tcc.common.repository;

import br.com.joaoluis.tcc.usuario.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FuncionarioRepository extends JpaRepository<Usuario, Long>, JpaSpecificationExecutor<Usuario> {
}
