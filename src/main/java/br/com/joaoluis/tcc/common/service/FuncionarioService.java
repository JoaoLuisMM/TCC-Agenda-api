package br.com.joaoluis.tcc.common.service;

import br.com.joaoluis.tcc.common.dto.EntityId;
import br.com.joaoluis.tcc.common.dto.FuncionarioIn;
import br.com.joaoluis.tcc.common.dto.FuncionarioOut;
import br.com.joaoluis.tcc.common.exception.RegraNegocioException;
import br.com.joaoluis.tcc.common.exception.ResourceNotFoundException;
import br.com.joaoluis.tcc.common.repository.FuncionarioRepository;
import br.com.joaoluis.tcc.common.repository.filter.FuncionarioFilter;
import br.com.joaoluis.tcc.common.util.TelefoneValidation;
import br.com.joaoluis.tcc.empresa.model.Empresa;
import br.com.joaoluis.tcc.seguranca.service.AuthenticationService;
import br.com.joaoluis.tcc.usuario.model.PapelUsuario;
import br.com.joaoluis.tcc.usuario.model.Usuario;
import br.com.joaoluis.tcc.usuario.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FuncionarioService {

    private final AuthenticationService authenticationService;
    private final FuncionarioRepository repository;
    private final PasswordUtil passwordUtil;

    public EntityId save(FuncionarioIn funcionario) {
        TelefoneValidation.validate(funcionario.celular());
        Usuario usuario = funcionario.toEntity();
        final Long empresaId = this.authenticationService.getEmpresaId();
        usuario.setEmpresa(Empresa.builder().id(empresaId).build());
        usuario.setEnabled(true);
        usuario.setPassword(passwordUtil.encode("agenda123"));
        // TODO enviar senha por e-mail
        this.repository.save(usuario);
        return new EntityId(usuario.getId());
    }

    public EntityId update(long id, FuncionarioIn funcionario) {
        TelefoneValidation.validate(funcionario.celular());
        final Usuario usuario = this.getById(id);
        BeanUtils.copyProperties(funcionario, usuario);
        usuario.setPapel(PapelUsuario.valueOf(funcionario.papel()));
        this.repository.save(usuario);
        return new EntityId(id);
    }

    public void updateSituacao(long id) {
        final Usuario usuario = this.getById(id);
        final Long usuarioLogadoId = this.authenticationService.getIdUsuarioLogado();
        if (id == usuarioLogadoId) {
            throw new RegraNegocioException("O usuário logado não pode atualização sua situação");
        }
        usuario.setEnabled(!usuario.getEnabled());
        this.repository.save(usuario);
    }

    public FuncionarioOut findById(long id) {
        final Usuario usuario = this.getById(id);
        return new FuncionarioOut(usuario.getId(), usuario.getNome(), usuario.getCpf(),
                usuario.getEmail(), usuario.getCelular(), !usuario.getEnabled(), usuario.getPapel());
    }

    public Page<FuncionarioOut> findAll(FuncionarioFilter filter, Pageable pageable) {
        long empresaId = authenticationService.getEmpresaId();
        Pageable pageableDefault = pageable;
        // Se não vem uma ordenação, cria uma ordenação padrão no campo dataCriacao na ordem decrescente
        if (pageable.getSort().isUnsorted()) {
            pageableDefault = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.Direction.ASC, "nome");
        }
        final Page<Usuario> usuarios = this.repository.findAll(filter.getRestricoes(empresaId), pageableDefault);
        return usuarios.map(FuncionarioOut::of);
    }

    public Usuario getById(long id) {
        return this.repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Funcionário com ID %d não encontrado", id)));
    }
}
