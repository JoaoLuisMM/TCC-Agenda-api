package br.com.joaoluis.tcc.clinicaveterinaria.service;

import br.com.joaoluis.tcc.clinicaveterinaria.dto.ClienteVeterinariaIn;
import br.com.joaoluis.tcc.clinicaveterinaria.dto.ClienteVeterinariaList;
import br.com.joaoluis.tcc.clinicaveterinaria.dto.EspecieClienteDto;
import br.com.joaoluis.tcc.clinicaveterinaria.dto.TutorDto;
import br.com.joaoluis.tcc.clinicaveterinaria.model.ClienteVeterinaria;
import br.com.joaoluis.tcc.clinicaveterinaria.model.Tutor;
import br.com.joaoluis.tcc.clinicaveterinaria.repository.ClienteVeterinariaRepository;
import br.com.joaoluis.tcc.clinicaveterinaria.repository.EspecieClienteRepository;
import br.com.joaoluis.tcc.clinicaveterinaria.repository.TutorRepository;
import br.com.joaoluis.tcc.clinicaveterinaria.repository.filter.ClienteVeterinariaFilter;
import br.com.joaoluis.tcc.common.dto.EntityId;
import br.com.joaoluis.tcc.common.exception.ResourceNotFoundException;
import br.com.joaoluis.tcc.empresa.model.Empresa;
import br.com.joaoluis.tcc.seguranca.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ClienteVeterinariaService {

    private final AuthenticationService authenticationService;
    private final ClienteVeterinariaRepository repository;
    private final EspecieClienteRepository especieClienteRepository;
    private final TutorRepository tutorRepository;

    public EntityId save(ClienteVeterinariaIn cliente) {
        if (!this.especieClienteRepository.existsById(cliente.especie().id())) {
            throw new ResourceNotFoundException(String.format("A espécie %s não foi encontrada", cliente.especie().descricao()));
        }
        ClienteVeterinaria novo = cliente.toEntity();
        long empresaId = this.authenticationService.getEmpresaId();
        novo.setEmpresa(Empresa.builder().id(empresaId).build());
        Optional<Tutor> tutor = this.tutorRepository.findByCpf(cliente.cpf());
        if (tutor.isPresent()) {
            novo.setTutor(tutor.get());
        } else {
            final Tutor novoTutor = this.tutorRepository.save(novo.getTutor());
            novo.setTutor(novoTutor);
        }
        return new EntityId(this.repository.save(novo).getId());
    }

    // TUDO ao editar um PET, podemos alterar os dados do TUTOR?
    public void update(long id, ClienteVeterinariaIn clienteIn) {
        if (!this.especieClienteRepository.existsById(clienteIn.especie().id())) {
            throw new ResourceNotFoundException(String.format("A espécie %s não foi encontrada", clienteIn.especie().descricao()));
        }
        final ClienteVeterinaria cliente = this.repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("O cliente com ID %d não foi encontrado", id)));
        clienteIn.merge(cliente);
        Optional<Tutor> tutor = this.tutorRepository.findByCpf(clienteIn.cpf());
        if (tutor.isPresent()) {
            cliente.setTutor(tutor.get());
        } else {
            Tutor novoTutor = Tutor.builder().nome(clienteIn.nomeTutor())
                    .cpf(clienteIn.cpf())
                    .email(clienteIn.email())
                    .celular(clienteIn.celular())
                    .build();
            cliente.setTutor(this.tutorRepository.save(novoTutor));
        }
        this.repository.save(cliente);
    }

    public ClienteVeterinaria findById(long clienteId) {
        return this.repository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException(String.format("Animal com ID %d não encontrado", clienteId)));
    }

    public Page<ClienteVeterinariaList> findAll(ClienteVeterinariaFilter filter, Pageable pageable) {
        long empresaId = authenticationService.getEmpresaId();
        Pageable pageableDefault = pageable;
        // Se não vem uma ordenação, cria uma ordenação padrão no campo dataCriacao na ordem decrescente
        if (pageable.getSort().isUnsorted()) {
            pageableDefault = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.Direction.DESC, "id");
        }
        final Page<ClienteVeterinaria> clientes = this.repository.findAll(filter.getRestricoes(empresaId), pageableDefault);

        return clientes.map(c -> new ClienteVeterinariaList(c.getId(), c.getCpf(), c.getNome(), c.getEmail(),
                new EspecieClienteDto(c.getEspecie().getId(), c.getEspecie().getDescricao()),
                new TutorDto(c.getTutor().getId(), c.getTutor().getNome(), c.getTutor().getEmail(), c.getTutor().getCelular()),
                c.getDataNascimento(), c.getCelular(), c.getRua(), c.getNumero(), c.getComplemento(),
                c.getCep(), c.getBairro(), c.getCidade()));
    }


    public List<ClienteVeterinaria> findClientesAgendamento() {
        long empresaId = authenticationService.getEmpresaId();
        ClienteVeterinariaFilter filter = new ClienteVeterinariaFilter();
        filter.setInativo(false);
        return this.repository.findAll(filter.getRestricoes(empresaId), Sort.by("nome"));
    }
}
