package br.com.joaoluis.tcc.common.service;

import br.com.joaoluis.tcc.agendamento.model.Agendamento;
import br.com.joaoluis.tcc.agendamento.repository.AgendamentoRepository;
import br.com.joaoluis.tcc.clinicaveterinaria.model.ClienteVeterinaria;
import br.com.joaoluis.tcc.clinicaveterinaria.model.EspecieCliente;
import br.com.joaoluis.tcc.clinicaveterinaria.model.Tutor;
import br.com.joaoluis.tcc.clinicaveterinaria.repository.ClienteVeterinariaRepository;
import br.com.joaoluis.tcc.clinicaveterinaria.repository.EspecieClienteRepository;
import br.com.joaoluis.tcc.clinicaveterinaria.repository.TutorRepository;
import br.com.joaoluis.tcc.common.model.Cliente;
import br.com.joaoluis.tcc.common.repository.ClienteRepository;
import br.com.joaoluis.tcc.common.repository.EmpresaRepository;
import br.com.joaoluis.tcc.empresa.model.Empresa;
import br.com.joaoluis.tcc.empresa.model.RamoAtividade;
import br.com.joaoluis.tcc.empresa.model.TipoPessoa;
import br.com.joaoluis.tcc.escritorioadvocacia.model.AgendamentoAdvocaticio;
import br.com.joaoluis.tcc.escritorioadvocacia.model.TipoCompromisso;
import br.com.joaoluis.tcc.usuario.model.PapelUsuario;
import br.com.joaoluis.tcc.usuario.model.Usuario;
import br.com.joaoluis.tcc.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class TesteService {

    private final EmpresaRepository empresaRepository;
    private final ClienteRepository clienteRepository;
    private final TutorRepository tutorRepository;
    private final ClienteVeterinariaRepository animalRepository;
    private final EspecieClienteRepository especieClienteRepository;
    //
    private final AgendamentoRepository agendamentoRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public void initiateData() {
        if (!this.existsRegisters()) {
            this.buildClinicaMedica();
            this.buildClinicaVeterinaria();
            this.buildEscritorioAdvocacia();
        }
    }

    public void buildClinicaMedica() {
        Empresa clinicaMedica = Empresa.builder()
                .nome("Clínica Médica")
                .cnpjCpf("1122233300011")
                .ramoAtividade(RamoAtividade.CLINICA_MEDICA)
                .tipoPessoa(TipoPessoa.PESSOA_JURIDICA)
                .celular("48999999999")
                .email("email@clinicamedica.com.br")
                .build();
        this.empresaRepository.save(clinicaMedica);
        Usuario usuarioClinicaMedica = Usuario.builder()
                .papel(PapelUsuario.ROLE_ADMINISTRADOR)
                .empresa(clinicaMedica)
                .cpf("89207495074")
                .nome("Admin clínica médica")
                .password(this.passwordEncoder.encode("agenda123"))
                .build();
        this.usuarioRepository.save(usuarioClinicaMedica);
        Usuario medico = Usuario.builder()
                .papel(PapelUsuario.ROLE_OPERACIONAL)
                .empresa(clinicaMedica)
                .cpf("63879849048")
                .email("medico@email.com")
                .nome("Médico")
                .password(this.passwordEncoder.encode("agenda123"))
                .build();
        this.usuarioRepository.save(medico);
        Usuario atendente = Usuario.builder()
                .papel(PapelUsuario.ROLE_RECEPCIONISTA)
                .empresa(clinicaMedica)
                .cpf("48103659053")
                .email("atendente@email.com")
                .nome("Atendente")
                .password(this.passwordEncoder.encode("agenda123"))
                .build();
        this.usuarioRepository.save(atendente);

        Cliente clienteClininaMedica = Cliente.builder()
                .empresa(clinicaMedica)
                .cpf("19162297007")
                .nome("Cliente 1")
                .email("cliente@email.com.br")
                .build();
                clienteClininaMedica.setInativo(false);
        clienteClininaMedica.setEmpresa(clinicaMedica);
        this.clienteRepository.save(clienteClininaMedica);
        LocalDateTime dataAtual = LocalDateTime.now();
        Agendamento agendamento = Agendamento.builder()
                .empresa(clinicaMedica)
                .especialista(medico)
                .cliente(clienteClininaMedica)
                .dataInicial(dataAtual)
                .dataFinal(dataAtual.plusMinutes(25))
                .build();
        this.agendamentoRepository.save(agendamento);
    }

    public void buildClinicaVeterinaria() {
        Empresa clinicaVeterinaria = Empresa.builder()
                .nome("Clinica Veterinária")
                .cnpjCpf("2233445566778")
                .ramoAtividade(RamoAtividade.CLINICA_VETERINARIA)
                .tipoPessoa(TipoPessoa.PESSOA_JURIDICA)
                .celular("48987654321")
                .email("email@clinicaveterinaria.com.br")
                .build();
        this.empresaRepository.save(clinicaVeterinaria);
        Usuario usuarioClinicaVeterinaria = Usuario.builder()
                .papel(PapelUsuario.ROLE_ADMINISTRADOR)
                .empresa(clinicaVeterinaria)
                .cpf("92991635029")
                .nome("Admin clínica veterinária")
                .password(this.passwordEncoder.encode("agenda123"))
                .build();
        this.usuarioRepository.save(usuarioClinicaVeterinaria);
        Usuario veterinario = Usuario.builder()
                .papel(PapelUsuario.ROLE_OPERACIONAL)
                .empresa(clinicaVeterinaria)
                .cpf("94121015037")
                .nome("Veterinario")
                .email("medico@clinicaveterinario.com")
                .password(this.passwordEncoder.encode("agenda123"))
                .build();
        this.usuarioRepository.save(veterinario);
        Usuario atendente = Usuario.builder()
                .papel(PapelUsuario.ROLE_RECEPCIONISTA)
                .empresa(clinicaVeterinaria)
                .cpf("20835765008")
                .nome("Atendente")
                .email("atendente@clinicaveterinario.com")
                .password(this.passwordEncoder.encode("agenda123"))
                .build();
        this.usuarioRepository.save(atendente);
        Tutor tutor = Tutor.builder()
                .cpf("87391969095")
                .nome("Gilberto")
                .email("clienteveterinario@email.com.br")
                .build();
        this.tutorRepository.save(tutor);
        EspecieCliente cachorro = EspecieCliente.builder().descricao("Cachorro").build();
        this.especieClienteRepository.save(cachorro);
        EspecieCliente gato = EspecieCliente.builder().descricao("Gato").build();
        this.especieClienteRepository.save(gato);
        ClienteVeterinaria clienteClininaVeterinaria = ClienteVeterinaria.clienteVeterinariaBuilder()
                .especie(cachorro)
                .cpf("53271458081")
                .nome("Spike")
                .tutor(tutor)
                .inativo(false)
                .build();
        clienteClininaVeterinaria.setEmpresa(clinicaVeterinaria);
        this.animalRepository.save(clienteClininaVeterinaria);
        LocalDateTime dataAtual = LocalDateTime.now();
        Agendamento agendamento = Agendamento.builder()
                .empresa(clinicaVeterinaria)
                .especialista(veterinario)
                .cliente(clienteClininaVeterinaria)
                .dataInicial(dataAtual)
                .dataFinal(dataAtual.plusMinutes(25))
                .build();
        this.agendamentoRepository.save(agendamento);
    }

    public void buildEscritorioAdvocacia() {
        Empresa escritorioAdvocacia = Empresa.builder()
                .nome("Escritório de advocacia")
                .cnpjCpf("12093318000143")
                .ramoAtividade(RamoAtividade.CONTRATO_SERVICO)
                .tipoPessoa(TipoPessoa.PESSOA_JURIDICA)
                .celular("48987654321")
                .email("email@escritorioadvocacia.com.br")
                .build();
        this.empresaRepository.save(escritorioAdvocacia);
        Usuario usuarioAdvocacia = Usuario.builder()
                .papel(PapelUsuario.ROLE_ADMINISTRADOR)
                .empresa(escritorioAdvocacia)
                .cpf("76090354082")
                .nome("Admin escritório advocacia")
                .password(this.passwordEncoder.encode("agenda123"))
                .build();
        this.usuarioRepository.save(usuarioAdvocacia);
        Usuario advogado = Usuario.builder()
                .papel(PapelUsuario.ROLE_OPERACIONAL)
                .empresa(escritorioAdvocacia)
                .cpf("73183231050")
                .nome("Advogado")
                .email("advogado@escritorioadvocacia.com")
                .password(this.passwordEncoder.encode("agenda123"))
                .build();
        this.usuarioRepository.save(advogado);
        Usuario atendente = Usuario.builder()
                .papel(PapelUsuario.ROLE_OPERACIONAL)
                .empresa(escritorioAdvocacia)
                .cpf("52819625070")
                .nome("Recepcionista")
                .email("recepcionista@escritorioadvocacia.com")
                .password(this.passwordEncoder.encode("agenda123"))
                .build();
        this.usuarioRepository.save(atendente);
        Cliente clienteClininaVeterinaria = Cliente.builder()
                .empresa(escritorioAdvocacia)
                .cpf("74812836050")
                .nome("Cliente 1")
                .inativo(false)
                .email("cliente1@email.com.br")
                .build();
        this.clienteRepository.save(clienteClininaVeterinaria);
        LocalDateTime dataAtual = LocalDateTime.now();

        AgendamentoAdvocaticio agendamento = AgendamentoAdvocaticio.builder()
                .empresa(escritorioAdvocacia)
                .especialista(advogado)
                .cliente(clienteClininaVeterinaria)
                .dataInicial(dataAtual)
                .dataFinal(dataAtual.plusMinutes(25))
                .numeroProcesso("TJSC-1234-653")
                .tipoCompromisso(TipoCompromisso.AUDIENCIA)
                .comarca("Comarca de São José")
                .build();
        this.agendamentoRepository.save(agendamento);
    }

    public boolean existsRegisters() {
        return this.usuarioRepository.count() > 0;
    }
}
