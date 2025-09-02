package br.com.dourado.pagamento.simplificado.api.controller;

import br.com.dourado.pagamento.simplificado.api.infra.exceptions.AccessDeniedException;
import br.com.dourado.pagamento.simplificado.api.infra.exceptions.BadRequestExeption;
import br.com.dourado.pagamento.simplificado.api.infra.exceptions.BasicAuthenticationEntryPointExeption;
import br.com.dourado.pagamento.simplificado.api.infra.exceptions.NotFoundExeption;
import br.com.dourado.pagamento.simplificado.api.infra.exceptions.ResponseExceptionDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NotFoundExeption.class)
    private ResponseEntity<ResponseExceptionDefault> handlerNotFoundExeption(NotFoundExeption exeption) {
        ResponseExceptionDefault response = new ResponseExceptionDefault(exeption.getTitulo(), exeption.getMenssagem());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(BadRequestExeption.class)
    private ResponseEntity<ResponseExceptionDefault> handlerBadRequestExeption(BadRequestExeption exeption) {
        ResponseExceptionDefault response = new ResponseExceptionDefault(exeption.getTitulo(), exeption.getMenssagem());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(BasicAuthenticationEntryPointExeption.class)
    private ResponseEntity<ResponseExceptionDefault> handlerBasicAuthenticationEntryPointExeption(BasicAuthenticationEntryPointExeption exeption) {
        ResponseExceptionDefault response = new ResponseExceptionDefault(exeption.getTitulo(), exeption.getMenssagem());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    private ResponseEntity<ResponseExceptionDefault> handlerAccessDeniedException(AccessDeniedException exeption) {
        ResponseExceptionDefault response = new ResponseExceptionDefault(exeption.getTitulo(), exeption.getMenssagem());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }
}
