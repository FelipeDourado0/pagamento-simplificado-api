package br.com.dourado.pagamento.simplificado.api.controller;

import br.com.dourado.pagamento.simplificado.api.infra.exceptions.AccessDeniedException;
import br.com.dourado.pagamento.simplificado.api.infra.exceptions.BadRequestExeption;
import br.com.dourado.pagamento.simplificado.api.infra.exceptions.BasicAuthenticationEntryPointExeption;
import br.com.dourado.pagamento.simplificado.api.infra.exceptions.NotFoundExeption;
import br.com.dourado.pagamento.simplificado.api.infra.exceptions.ResponseExceptionDefault;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void deveRetornarNotFoundException() {
        // given
        NotFoundExeption ex = new NotFoundExeption("Recurso não encontrado", "Usuário não existe");

        // when
        ResponseEntity<ResponseExceptionDefault> response = invokeHandlerNotFound(ex);

        // then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Recurso não encontrado", response.getBody().getTitulo());
        assertEquals("Usuário não existe", response.getBody().getDescicao());
    }

    @Test
    void deveRetornarBadRequestException() {
        BadRequestExeption ex = new BadRequestExeption("Requisição inválida", "CPF inválido");

        ResponseEntity<ResponseExceptionDefault> response = invokeHandlerBadRequest(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Requisição inválida", response.getBody().getTitulo());
        assertEquals("CPF inválido", response.getBody().getDescicao());
    }

    @Test
    void deveRetornarUnauthorizedException() {
        BasicAuthenticationEntryPointExeption ex = new BasicAuthenticationEntryPointExeption(
                "Erro de autenticação", "Credenciais inválidas");

        ResponseEntity<ResponseExceptionDefault> response = invokeHandlerUnauthorized(ex);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Erro de autenticação", response.getBody().getTitulo());
        assertEquals("Credenciais inválidas", response.getBody().getDescicao());
    }

    @Test
    void deveRetornarAccessDeniedException() {
        AccessDeniedException ex = new AccessDeniedException("Acesso negado", "Você não tem permissão");

        ResponseEntity<ResponseExceptionDefault> response = invokeHandlerForbidden(ex);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Acesso negado", response.getBody().getTitulo());
        assertEquals("Você não tem permissão", response.getBody().getDescicao());
    }

    // Métodos helpers para chamar os handlers privados via reflection
    private ResponseEntity<ResponseExceptionDefault> invokeHandlerNotFound(NotFoundExeption ex) {
        return exceptionHandler.handlerNotFoundExeption(ex);
    }

    private ResponseEntity<ResponseExceptionDefault> invokeHandlerBadRequest(BadRequestExeption ex) {
        return exceptionHandler.handlerBadRequestExeption(ex);
    }

    private ResponseEntity<ResponseExceptionDefault> invokeHandlerUnauthorized(BasicAuthenticationEntryPointExeption ex) {
        return exceptionHandler.handlerBasicAuthenticationEntryPointExeption(ex);
    }

    private ResponseEntity<ResponseExceptionDefault> invokeHandlerForbidden(AccessDeniedException ex) {
        return exceptionHandler.handlerAccessDeniedException(ex);
    }
}