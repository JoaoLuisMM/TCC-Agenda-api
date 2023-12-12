package br.com.joaoluis.tcc.agendamento.validation;

import br.com.joaoluis.tcc.agendamento.dto.AgendamentoIn;
import br.com.joaoluis.tcc.agendamento.model.Agendamento;
import br.com.joaoluis.tcc.agendamento.model.SituacaoAgendamento;
import br.com.joaoluis.tcc.agendamento.repository.AgendamentoRepository;
import br.com.joaoluis.tcc.common.exception.ValidationException;
import br.com.joaoluis.tcc.usuario.repository.UsuarioRepository;
import jakarta.persistence.criteria.Predicate;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Component
public class AgendamentoValidation {

    private final Validator validator;
    private final AgendamentoRepository repository;
    private final UsuarioRepository usuarioRepository;

    public void validate(AgendamentoIn agendamento) {
        this.validate(null, agendamento);
    }

    public void validate(Long agendamentoId, AgendamentoIn agendamento) {
        List<String> erros = new ArrayList<>();
        Set<ConstraintViolation<AgendamentoIn>> violations = this.validator.validate(agendamento);
        if (!violations.isEmpty()) {
            for (ConstraintViolation<AgendamentoIn> constraintViolation : violations) {
                erros.add(constraintViolation.getMessage());
            }
        }
        if (agendamento.getDataInicial() != null && agendamento.getDataFinal() != null) {
            if (agendamento.getDataInicial().isBefore(LocalDateTime.now().withNano(0))) {
                erros.add("A data de início deve ser maior do que a data atual");
            }
            LocalDateTime dataFinalCom20Minutos = agendamento.getDataInicial().plusMinutes(20);
            if (agendamento.getDataFinal().isBefore(dataFinalCom20Minutos)) {
                erros.add("A data de fim deve ser no mínimo 20 minutos maior do que a data inicial");
            }
            if (agendamento.getEspecialista() != null) {
                long doutorId = agendamento.getEspecialista().getId();
                Specification<Agendamento> specification = this.dataInicialJaCadadastrada(agendamento.getDataInicial(), agendamento.getDataFinal())
                        .or(this.dataFinalJaCadadastrada(agendamento.getDataInicial(), agendamento.getDataFinal()))
                        .and(this.verificaEspecialistaId(doutorId))
                        .and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("situacao"), SituacaoAgendamento.ATIVO));
                if (agendamentoId != null) {
                    specification = specification.and(this.verificaAgendamentoId(agendamentoId));
                }
                if (this.repository.exists(specification)) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                    String mensagem = "Já existe um agendamento para a data de início %s";
                    erros.add(String.format(mensagem, agendamento.getDataInicial().format(formatter)));
                }
            }
        }
        if (agendamento.getEspecialista() != null) {
            if (!this.usuarioRepository.existsById(agendamento.getEspecialista().getId())) {
                erros.add(String.format("O responsável com ID %d não existe", agendamento.getEspecialista().getId()));
            }
        }
        if (!erros.isEmpty()) {
            throw new ValidationException("Erro de validação", erros);
        }
    }

    private Specification<Agendamento> dataInicialJaCadadastrada(Long agendamentoId, long doutorId, LocalDateTime dataInicial) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicateDoutor = criteriaBuilder.equal(root.get("especialista").get("id"), doutorId);
            Predicate predicateDataInicial = criteriaBuilder.equal(root.get("dataInicial"), dataInicial);
            if (agendamentoId != null) {
                Predicate predicateAgendamentoId = criteriaBuilder.notEqual(root.get("id"), agendamentoId);
                return criteriaBuilder.and(predicateDoutor, predicateDataInicial, predicateAgendamentoId);
            }
            return criteriaBuilder.and(predicateDoutor, predicateDataInicial);
        };
    }

    private Specification<Agendamento> verificaAgendamentoId(long agendamentoId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.notEqual(root.get("id"), agendamentoId);
    }

    private Specification<Agendamento> verificaEspecialistaId(long doutorId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("especialista").get("id"), doutorId);
    }

    private Specification<Agendamento> dataInicialJaCadadastrada(LocalDateTime dataInicial, LocalDateTime dataFinal) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.and(criteriaBuilder.between(root.get("dataInicial"), dataInicial, dataFinal));
    }

    private Specification<Agendamento> dataFinalJaCadadastrada(LocalDateTime dataInicial, LocalDateTime dataFinal) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.and(criteriaBuilder.between(root.get("dataFinal"), dataInicial, dataFinal));
    }
}
