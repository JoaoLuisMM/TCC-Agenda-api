package br.com.joaoluis.tcc.seguranca.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Setter
@Getter
@ResponseStatus(HttpStatus.FORBIDDEN)
public class UserUnauthorizedException extends RuntimeException {

    public UserUnauthorizedException(String mensagem) {
        super(mensagem);
    }

    public UserUnauthorizedException(Throwable cause, String mensagem) {
        super(mensagem, cause);
    }
}

