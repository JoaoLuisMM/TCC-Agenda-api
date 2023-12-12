package br.com.joaoluis.tcc.common.util;

import br.com.joaoluis.tcc.common.exception.RegraNegocioException;

public class TelefoneValidation {

    public static void validate(String telefone) {
        if (telefone != null && (telefone.length() < 10 || telefone.length() > 11)) {
            throw new RegraNegocioException("O número de telefone/celular deve ter 10 ou 11 caracteres");
        }
    }
}
