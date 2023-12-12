package br.com.joaoluis.tcc.common.service;

import br.com.joaoluis.tcc.common.dto.ClienteIn;
import br.com.joaoluis.tcc.common.dto.ClienteList;
import br.com.joaoluis.tcc.common.dto.ClienteOut;
import br.com.joaoluis.tcc.common.dto.EntityId;
import br.com.joaoluis.tcc.common.exception.RegraNegocioException;
import br.com.joaoluis.tcc.common.exception.ResourceNotFoundException;
import br.com.joaoluis.tcc.common.model.Cliente;
import br.com.joaoluis.tcc.common.repository.ClienteRepository;
import br.com.joaoluis.tcc.common.repository.filter.ClienteFilter;
import br.com.joaoluis.tcc.common.util.TelefoneValidation;
import br.com.joaoluis.tcc.seguranca.service.AuthenticationService;
import br.com.joaoluis.tcc.usuario.model.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ClienteService {

    private final AuthenticationService authenticationService;
    private final ClienteRepository repository;

    public EntityId save(ClienteIn clienteIn) {
        TelefoneValidation.validate(clienteIn.celular());
        final Usuario usuario = this.authenticationService.getUsuario();
        if (this.repository.existsByCpfAndEmpresaId(clienteIn.cpf(), usuario.getEmpresaId())) {
            throw new RegraNegocioException(String.format("Já existe um cliente com o número de CPF %s", clienteIn.cpf()));
        }
        Cliente cliente = clienteIn.toEntity();
        cliente.setEmpresa(usuario.getEmpresa());
        this.repository.save(cliente);
        return new EntityId(cliente.getId());
    }

    public EntityId update(long id, ClienteIn clienteIn) {
        TelefoneValidation.validate(clienteIn.celular());
        final Cliente cliente = this.repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Cliente com ID %d não encontrado", id)));
        BeanUtils.copyProperties(clienteIn, cliente, "cpf");
        this.repository.save(cliente);
        return new EntityId(id);
    }

    public ClienteOut findById(long id) {
        return this.repository.findById(id).map(c -> new ClienteOut(c.getId(), c.getCpf(), c.getNome(), c.getEmail(),
                        c.getDataNascimento(), c.getCelular(), c.getRua(), c.getNumero(), c.getComplemento(),
                        c.getCep(), c.getBairro(), c.getCidade()))
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Cliente com ID %d não encontrado", id)));
    }

    public Page<ClienteList> findAll(ClienteFilter filter, Pageable pageable) {
        long empresaId = authenticationService.getEmpresaId();
        Pageable pageableDefault = pageable;
        // Se não vem uma ordenação, cria uma ordenação padrão no campo dataCriacao na ordem decrescente
        if (pageable.getSort().isUnsorted()) {
            pageableDefault = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.Direction.DESC, "id");
        }
        final Page<Cliente> clientes = this.repository.findAll(filter.getRestricoes(empresaId), pageableDefault);
        return clientes.map(c -> new ClienteList(c.getId(), c.getCpf(), c.getNome(), c.getEmail(),
                c.getDataNascimento(), c.getCelular(), c.getRua(), c.getNumero(), c.getComplemento(),
                c.getCep(), c.getBairro(), c.getCidade()));
    }

    public List<Cliente> findClientesAgendamento() {
        long empresaId = authenticationService.getEmpresaId();
        ClienteFilter filter = new ClienteFilter();
        filter.setInativo(false);
        return this.repository.findAll(filter.getRestricoes(empresaId), Sort.by("nome"));
    }
}
