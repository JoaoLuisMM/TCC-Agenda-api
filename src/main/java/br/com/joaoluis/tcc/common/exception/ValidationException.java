package br.com.joaoluis.tcc.common.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ValidationException extends RuntimeException {

    List<String> errors = new ArrayList<>();

    public ValidationException() {
    }

    public ValidationException(String mensagem) {
        super(mensagem);
    }

    public ValidationException(String mensagem, List<String> errors) {
        super(mensagem);
        this.errors = errors;
    }

    @Override
    public String toString() {
        return ""+this.getClass();
    }
}

