package br.com.dourado.pagamento.simplificado.api.infra.exceptions;

public class BadRequestExeption extends BaseExeption {
    public BadRequestExeption(String titulo, String menssagem) {
        super(titulo, menssagem);
    }
}
