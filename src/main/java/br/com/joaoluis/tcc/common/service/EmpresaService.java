package br.com.joaoluis.tcc.common.service;

import br.com.joaoluis.tcc.common.exception.RegraNegocioException;
import br.com.joaoluis.tcc.common.repository.EmpresaRepository;
import br.com.joaoluis.tcc.empresa.dto.EmpresaIn;
import br.com.joaoluis.tcc.empresa.dto.ResponsavelIn;
import br.com.joaoluis.tcc.empresa.model.Empresa;
import br.com.joaoluis.tcc.empresa.validation.EmpresaValidation;
import br.com.joaoluis.tcc.usuario.model.PapelUsuario;
import br.com.joaoluis.tcc.usuario.model.Usuario;
import br.com.joaoluis.tcc.usuario.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class EmpresaService {

    private final EmpresaRepository repository;
    private final EmpresaValidation validation;
    private final UsuarioService usuarioService;

    @Transactional
    public long save(EmpresaIn empresa) {
        // Não é permitido o cadastro de empresas com o mesmo CNPJ/CPF
        if (this.repository.existsByCnpjCpf(empresa.cnpjCpf())) {
            throw new RegraNegocioException(String.format("Já existe uma empresa com o CNPJ/CPF %s", empresa.cnpjCpf()));
        }
        /*
        Não é permitido o cadastro de usuários com o mesmo CPF, mesmo que em empresas diferentes. Isto seria um dos pontos
        negativos para a abordagem de fazer o cadastro de todas as empresas e usuários na mesma base de dados. Como o
        login do usuário é feito apenas pelo CPF, não podemos repetí-lo.
        */
        if (this.usuarioService.existsByCpf(empresa.responsavel().cpf())) {
            throw new RegraNegocioException(String.format("Já existe um usuário com CPF %s", empresa.responsavel().cpf()));
        }
        this.validation.validate(empresa);
        Empresa nova = empresa.toEntity();
        this.repository.save(nova);
        this.usuarioService.createByEmpresa(nova, empresa.responsavel());
        return nova.getId();
    }

    public Optional<Empresa> findById(long id) {
        return this.repository.findById(id);
    }
}
