package br.com.joaoluis.tcc.common.exception.handler;

import br.com.joaoluis.tcc.common.error.Error;
import br.com.joaoluis.tcc.common.exception.RegraNegocioException;
import br.com.joaoluis.tcc.common.exception.ResourceNotFoundException;
import br.com.joaoluis.tcc.common.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@ControllerAdvice
public class AgendamentoExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleLicException(Exception ex) {
        String mensagemUsuario = "Ocorreu um erro no servidor: " + ex.getLocalizedMessage();
        ex.printStackTrace();
        List<Error> erros = Arrays.asList(Error.builder().message(mensagemUsuario).build());
        return ResponseEntity.internalServerError().body(erros);
    }

    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ex.printStackTrace();
        String mensagemUsuario = "Não foi possível converter o objeto a partir do JSON";
        List<Error> erros = Arrays.asList(Error.builder().message(mensagemUsuario).build());
        return handleExceptionInternal(ex, erros, headers, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String mensagemUsuario = "Método não encontrado";
        List<Error> erros = Arrays.asList(Error.builder().message(mensagemUsuario).build());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(erros);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<Error> erros = this.createListErrors(ex.getBindingResult());
        return ResponseEntity.badRequest().body(erros);
    }

    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        String mensagemUsuario = ex.getMessage();
        List<Error> erros = Arrays.asList(Error.builder().message(mensagemUsuario).build());
        return handleExceptionInternal(ex, erros, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler({ValidationException.class})
    public ResponseEntity<Object> handleValidationException(ValidationException ex, WebRequest request) {
        List<Error> erros = new ArrayList<>();
        ex.getErrors().forEach(mensagem -> {
            erros.add(Error.builder().message(mensagem).build());
        });
        return handleExceptionInternal(ex, erros, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({ RegraNegocioException.class })
    public ResponseEntity<Object> handleRegraNegocioException(RegraNegocioException ex, WebRequest request) {
        String mensagemUsuario = ex.getMensagem();
        List<Error> erros = Arrays.asList(Error.builder().message(mensagemUsuario).build());
        return handleExceptionInternal(ex, erros, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    public List<Error> createListErrors(BindingResult bindingResult) {
        List<Error> erros = new ArrayList<>();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            String mensagemUsuario = messageSource.getMessage(fieldError, LocaleContextHolder.getLocale());
            erros.add(Error.builder().message(mensagemUsuario).build());
        }
        return erros;
    }

}
