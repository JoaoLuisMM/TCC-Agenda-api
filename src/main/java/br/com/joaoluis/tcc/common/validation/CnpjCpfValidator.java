package br.com.joaoluis.tcc.common.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CnpjCpfValidator {

    final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    final private String CNPJ_ZEROS = "00000000000000";

    private boolean pessoaJuridica;
    private String numeroDocumento;

    public List<String> validate() {
        if (this.pessoaJuridica) {
            if (this.numeroDocumento.equals(this.CNPJ_ZEROS)) {
                return this.createListErrorsCnpjZero();
            }
            CNPJValidator cnpj = CNPJValidator.builder().cnpj(this.numeroDocumento).build();
            return this.createListErrors(validator.validate(cnpj));
        }
        CPFValidator cpf = CPFValidator.builder().cpf(this.numeroDocumento).build();
        return this.createListErrors(validator.validate(cpf));
    }

    public CnpjCpfValidator(boolean pessoaJuridica, String numeroDocumento) {
        this.pessoaJuridica = pessoaJuridica;
        this.numeroDocumento = numeroDocumento;
    }

    public List<String> createListErrors(Set<ConstraintViolation<Object>> violations) {
        List<String> erros = new ArrayList<>();
        if (!violations.isEmpty()) {
            violations.stream().forEach(v -> {
                erros.add(v.getMessage());
            });
        }
        return erros;
    }

    public List<String> createListErrorsCnpjZero() {
        List<String> erros = new ArrayList<>();
        erros.add("CNPJ inválido. Por favor, informe corretamente");
        return erros;
    }

    public static void main(String[] args) {
        System.out.println("false: "+new CnpjCpfValidator(false, "83064741000163").validate());
        System.out.println("false: "+new CnpjCpfValidator(false, "00000000000").validate());
        System.out.println("true: "+new CnpjCpfValidator(true, "83064741000163").validate());
        System.out.println("true: "+new CnpjCpfValidator(true, "00000000000000").validate());
        System.out.println("true: "+new CnpjCpfValidator(true, "00000000000191").validate());
    }
}
