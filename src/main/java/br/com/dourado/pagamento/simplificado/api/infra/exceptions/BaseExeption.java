package br.com.dourado.pagamento.simplificado.api.infra.exceptions;

import lombok.Getter;

@Getter
public abstract class BaseExeption extends RuntimeException {
    private final String titulo;
    private final String menssagem;

    protected BaseExeption(String titulo, String menssagem) {
        super();
        this.titulo = titulo;
        this.menssagem = menssagem;
    }
}
