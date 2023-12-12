package br.com.joaoluis.tcc.seguranca.service;

import br.com.joaoluis.tcc.seguranca.exception.UserUnauthorizedException;
import br.com.joaoluis.tcc.seguranca.model.EmpresaResumo;
import br.com.joaoluis.tcc.seguranca.model.UsuarioLogado;
import br.com.joaoluis.tcc.seguranca.model.UsuarioSistema;
import br.com.joaoluis.tcc.usuario.model.Usuario;
import br.com.joaoluis.tcc.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collection;

@RequiredArgsConstructor
@Service
public class AuthenticationService implements UserDetailsService {
	
	private final UsuarioRepository repository;

	@Transactional(readOnly = true)
	@Override
	public UserDetails loadUserByUsername(String cpf) throws UsernameNotFoundException {
		Usuario usuario = this.repository.findByCpf(cpf)
				.orElseThrow(() -> new UsernameNotFoundException("Usuário e/ou senha incorretos"));
		UsuarioSistema usuarioSistema = new UsuarioSistema(usuario.getCpf(), usuario.getPassword(), usuario.getEnabled(), this.getPermissoes(usuario));
		usuarioSistema.setId(usuario.getId());
		usuarioSistema.setCpf(usuario.getCpf());
		usuarioSistema.setNome(usuario.getNome());
		usuarioSistema.setEmpresa(new EmpresaResumo(usuario.getEmpresa()));
		usuarioSistema.setEmail(usuario.getEmail());
		usuarioSistema.setInativo(!usuario.getEnabled());
		usuarioSistema.setPermissao(usuario.getPapel().name());
		return usuarioSistema;
	}

	private Collection<? extends GrantedAuthority> getPermissoes(Usuario usuario) {
		return Arrays.asList(new SimpleGrantedAuthority(usuario.getPapel().name()));
	}

	public UsuarioLogado getUsuarioLogado() {
		final UsuarioLogado usuarioLogado = new UsuarioLogado();
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
			throw new UserUnauthorizedException("Usuário permissão ou não logado");
		}
		Usuario usuario = this.repository.findByCpf(authentication.getName())
				.orElseThrow(() -> new UserUnauthorizedException("Usuário sem permissão ou não logado"));
		usuarioLogado.setId(usuario.getId());
		usuarioLogado.setNome(usuario.getNome());
		usuarioLogado.setEmail(usuario.getEmail());
		usuarioLogado.setEmpresa(new EmpresaResumo(usuario.getEmpresa()));
		usuarioLogado.setPermissao(authentication.getAuthorities().stream().findFirst().get().getAuthority());
		return usuarioLogado;
	}

	public Usuario getUsuario() {
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
			throw new UserUnauthorizedException("Usuário sem permissão ou não logado");
		}
		return this.repository.findByCpf(authentication.getName())
				.orElseThrow(() -> new UserUnauthorizedException("Usuário sem permissão ou não logado"));
	}

	public Long getEmpresaId() {
		return this.getUsuarioLogado() != null ? this.getUsuarioLogado().getEmpresa().id() : null;
	}

	public Long getIdUsuarioLogado() {
		return this.getUsuarioLogado() != null ? this.getUsuarioLogado().getId() : null;
	}
}
