package br.com.joaoluis.tcc.empresa.validation;

import br.com.joaoluis.tcc.common.exception.ValidationException;
import br.com.joaoluis.tcc.common.validation.CnpjCpfValidator;
import br.com.joaoluis.tcc.empresa.dto.EmpresaIn;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Component
public class EmpresaValidation {

    private final Validator validator;

    public void validate(EmpresaIn empresa) {
        List<String> erros = new ArrayList<>();
        Set<ConstraintViolation<EmpresaIn>> violations = this.validator.validate(empresa);
        if (!violations.isEmpty()) {
            for (ConstraintViolation<EmpresaIn> constraintViolation : violations) {
                erros.add(constraintViolation.getMessage());
            }
        }
        erros.addAll(new CnpjCpfValidator(empresa.isPessoaJuridica(), empresa.cnpjCpf()).validate());
        if (!erros.isEmpty()) {
            throw new ValidationException("Erro de validação", erros);
        }
    }
}
