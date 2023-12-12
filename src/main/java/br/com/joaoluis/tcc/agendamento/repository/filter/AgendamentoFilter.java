package br.com.joaoluis.tcc.agendamento.repository.filter;

import br.com.joaoluis.tcc.agendamento.model.Agendamento;
import br.com.joaoluis.tcc.agendamento.model.SituacaoAgendamento;
import br.com.joaoluis.tcc.common.exception.RegraNegocioException;
import br.com.joaoluis.tcc.empresa.model.Empresa;
import jakarta.persistence.criteria.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

import static org.springframework.data.jpa.domain.Specification.where;

@Setter
@Getter
@ToString
public class AgendamentoFilter<T> {

    private Long id;
    private String nome;
    private Long empresaId;
    private Long especialistaId;
    private LocalDateTime dataInicial;
    private LocalDateTime dataFinal;
    private SituacaoAgendamento situacao;

    public Specification<T> getRestricoes() {
        if (this.empresaId == null) {
            throw new RegraNegocioException("O id da empresa é obrigatório");
        }
        if (especialistaId == null) {
            if (dataInicial == null) {
                this.dataInicial = LocalDateTime.now().with(TemporalAdjusters.firstDayOfMonth()).withHour(0).withMinute(1);
            } else {
                this.dataInicial = this.dataInicial.with(TemporalAdjusters.firstDayOfMonth()).withHour(0).withMinute(1);
            }
            if (dataFinal == null) {
                this.dataFinal = LocalDateTime.now().with(TemporalAdjusters.lastDayOfMonth()).withHour(23).withMinute(59);
            } else {
                this.dataFinal = this.dataFinal.with(TemporalAdjusters.lastDayOfMonth()).withHour(23).withMinute(59);
            }
        }
        Specification where = this.wherePadrao();
        if (id != null && id > 0) {
            where = addClausula(where, whereId());
        }
        if (nome != null && !nome.isEmpty()) {
            where = addClausula(where, whereNome());
        }
        if (situacao != null) {
            where = addClausula(where, whereSituacao());
        }

        return where;
    }

    private Specification addClausula(Specification where, Specification novaClausula){
        if(where == null){
            return where(novaClausula);
        } else {
            return where(where).and(novaClausula);
        }
    }

    private Specification wherePadrao() {
        return new Specification<Agendamento>() {
            @Override
            public Predicate toPredicate(Root<Agendamento> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                Join<Agendamento, Empresa> empresa = root.join("empresa");
                return builder.and(
                        builder.equal(empresa.get("id"), empresaId),
                        builder.equal(root.get("situacao"), SituacaoAgendamento.ATIVO),
                        builder.between(root.get("dataInicial"), dataInicial, dataFinal)
                );
            }
        };
    }

    private Specification whereId() {
        return new Specification<Agendamento>() {
            @Override
            public Predicate toPredicate(Root<Agendamento> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                return builder.and(builder.equal(root.get("id"), id));
            }
        };
    }

    private Specification whereSituacao() {
        return new Specification<Agendamento>() {
            @Override
            public Predicate toPredicate(Root<Agendamento> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                return builder.and(builder.equal(root.get("situacao"), situacao));
            }
        };
    }

    private Specification whereNome() {
        return new Specification<Agendamento>() {
            @Override
            public Predicate toPredicate(Root<Agendamento> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                String nomeCliente = "%" + nome + "%";
                return builder.like(builder.lower(root.get("nome")), nomeCliente.toLowerCase());
            }
        };
    }

}
