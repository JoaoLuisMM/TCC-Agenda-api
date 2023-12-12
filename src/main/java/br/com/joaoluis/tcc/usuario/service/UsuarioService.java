package br.com.joaoluis.tcc.usuario.service;

import br.com.joaoluis.tcc.common.exception.RegraNegocioException;
import br.com.joaoluis.tcc.common.exception.ResourceNotFoundException;
import br.com.joaoluis.tcc.common.repository.EmpresaRepository;
import br.com.joaoluis.tcc.empresa.dto.ResponsavelIn;
import br.com.joaoluis.tcc.empresa.model.Empresa;
import br.com.joaoluis.tcc.usuario.dto.UsuarioEdit;
import br.com.joaoluis.tcc.usuario.dto.UsuarioIn;
import br.com.joaoluis.tcc.usuario.dto.UsuarioList;
import br.com.joaoluis.tcc.usuario.model.PapelUsuario;
import br.com.joaoluis.tcc.usuario.model.Usuario;
import br.com.joaoluis.tcc.usuario.repository.UsuarioRepository;
import br.com.joaoluis.tcc.usuario.repository.filter.UsuarioFilter;
import br.com.joaoluis.tcc.usuario.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UsuarioService {
	
	private final UsuarioRepository repository;
	private final EmpresaRepository empresaRepository;
	private final PasswordUtil passwordUtil;

	@Transactional
	public long save(UsuarioIn usuario) {
		this.validateNewUserData(usuario);
		Usuario novoUsuario = usuario.toEntity();
		novoUsuario.setPassword(passwordUtil.encode(usuario.getPassword()));
		this.repository.save(novoUsuario);
		return novoUsuario.getId();
	}

	public void validateNewUserData(UsuarioIn usuario) {
		if (!this.empresaRepository.existsById(usuario.getEmpresa().getId())) {
			throw new ResourceNotFoundException(String.format("Empresa com ID %d não encontrada", usuario.getEmpresa().getId()));
		}
		if (this.repository.existsByCpf(usuario.getCpf())) {
			throw new RegraNegocioException(String.format("Já existe um usuário com o CPF %s", usuario.getCpf()));
		}
		// TODO validar a igualdade da senha e contrasenha?
	}

	@Transactional
	public void update(Long id, UsuarioEdit usuario) {
		this.validateUpdatedUserData(id, usuario);
		Usuario usuarioBd = this.getOne(id);
		usuario.merge(usuarioBd);
		this.repository.save(usuarioBd);
	}

	public void validateUpdatedUserData(long usuarioId, UsuarioEdit usuario) {
		if (!this.empresaRepository.existsById(usuario.getEmpresa().getId())) {
			throw new ResourceNotFoundException(String.format("Empresa com ID %d não encontrada", usuario.getEmpresa().getId()));
		}
		if (this.repository.existsByCpfAndIdNot(usuario.getCpf(), usuarioId)) {
			throw new RegraNegocioException(String.format("Já existe um usuário com o CPF %s", usuario.getCpf()));
		}
	}

	public void createByEmpresa(Empresa empresa, ResponsavelIn responsavelIn) {
		Usuario responsavel = responsavelIn.toEntity();
		responsavel.setEmpresa(empresa);
		responsavel.setPapel(PapelUsuario.ROLE_ADMINISTRADOR);
		responsavel.setPassword(this.passwordUtil.encode(responsavelIn.senha()));
		this.repository.save(responsavel);
	}

	public Optional<Usuario> findById(Long usuarioId) {
		return this.repository.findById(usuarioId);
	}

	public Usuario getOne(Long usuarioId) {
		return this.findById(usuarioId)
			.orElseThrow(() -> new ResourceNotFoundException(String.format("Usuario com ID %d não encontrado", Arrays.asList(usuarioId))));
	}

	public Page<UsuarioList> findAll(UsuarioFilter filter, Pageable pageable) {
		final Pageable paginacao = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.Direction.ASC,"nome");
		return repository.findAll(filter.getRestricoes(), paginacao).map(u -> new UsuarioList(u));
	}

    public boolean existsByCpf(String cpf) {
		return this.repository.existsByCpf(cpf);
    }
}
