package br.com.joaoluis.tcc.common.repository.filter;

import br.com.joaoluis.tcc.common.exception.RegraNegocioException;
import br.com.joaoluis.tcc.empresa.model.Empresa;
import br.com.joaoluis.tcc.usuario.model.PapelUsuario;
import br.com.joaoluis.tcc.usuario.model.Usuario;
import jakarta.persistence.criteria.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.jpa.domain.Specification;

import static org.springframework.data.jpa.domain.Specification.where;

@Setter
@Getter
@ToString
public class FuncionarioFilter {
    private Long id;
    private String nome;
    @Setter(AccessLevel.PRIVATE)
    private Long empresaId;
    private Boolean inativo;

    public Specification<Usuario> getRestricoes(Long empresaId) {
        this.empresaId = empresaId;
        if (this.empresaId == null) {
            throw new RegraNegocioException("O id da empresa é obrigatório");
        }
        Specification where = this.wherePadrao();
        if (id != null && id > 0) {
            where = addClausula(where, whereId());
        }
        if (nome != null && !nome.isEmpty()) {
            where = addClausula(where, whereNome());
        }
        if (inativo != null) {
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

    /**
     * Filtra os usuários associados à empresa do usuário logado.
     * @return
     */
    private Specification wherePadrao() {
        return new Specification<Usuario>() {
            @Override
            public Predicate toPredicate(Root<Usuario> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                Join<Usuario, Empresa> empresa = root.join("empresa");
                return builder.and(builder.equal(empresa.get("id"), empresaId));
            }
        };
    }

    private Specification whereId() {
        return new Specification<Usuario>() {
            @Override
            public Predicate toPredicate(Root<Usuario> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                return builder.and(builder.equal(root.get("id"), id));
            }
        };
    }

    private Specification whereNome() {
        return new Specification<Usuario>() {
            @Override
            public Predicate toPredicate(Root<Usuario> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                String nomeMedico = "%" + nome + "%";
                return builder.like(builder.lower(root.get("nome")), nomeMedico.toLowerCase());
            }
        };
    }

    private Specification whereSituacao() {
        return new Specification<Usuario>() {
            @Override
            public Predicate toPredicate(Root<Usuario> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                return builder.and(builder.equal(root.get("enabled"), inativo));
            }
        };
    }
}
