package br.com.joaoluis.tcc.common.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Setter
@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    private String mensagem;

    public ResourceNotFoundException(Throwable e, String message) {
        super(message, e);
        this.mensagem = mensagem;
    }

    public ResourceNotFoundException(String message) {
        super(message);
        this.mensagem = mensagem;
    }
}
