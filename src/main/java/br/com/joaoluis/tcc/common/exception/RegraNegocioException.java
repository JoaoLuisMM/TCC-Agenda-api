package br.com.joaoluis.tcc.common.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Setter
@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RegraNegocioException extends RuntimeException {

    private String mensagem;

    public RegraNegocioException(String mensagem) {
        super(mensagem);
        this.mensagem = mensagem;
    }

    public RegraNegocioException(Throwable cause, String mensagem) {
        super(mensagem, cause);
        this.mensagem = mensagem;
    }
}

