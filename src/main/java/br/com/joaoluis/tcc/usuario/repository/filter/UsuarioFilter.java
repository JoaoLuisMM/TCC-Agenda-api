package br.com.joaoluis.tcc.usuario.repository.filter;

import br.com.joaoluis.tcc.empresa.model.Empresa;
import br.com.joaoluis.tcc.usuario.model.Usuario;
import jakarta.persistence.criteria.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

import static org.springframework.data.jpa.domain.Specification.where;

@Setter
@Getter
public class UsuarioFilter {

    private Long id;
    private Long empresaId;
    private String nome;

    public Specification<Usuario> getRestricoes() {
        Specification where = this.wherePadrao();
        if (id != null && id > 0) {
            where = addClausula(where, whereId());
        }
        if (nome != null && !nome.isEmpty()) {
            where = addClausula(where, whereNome());
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
                String nomeCliente = "%" + nome + "%";
                return builder.like(builder.lower(root.get("nome")), nomeCliente.toLowerCase());
            }
        };
    }
}
