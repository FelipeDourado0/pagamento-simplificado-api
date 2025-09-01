package br.com.dourado.pagamento.simplificado.api.infra.exceptions;

public class NotFoundExeption extends BaseExeption {
    public NotFoundExeption(String titulo, String menssagem) {
        super(titulo, menssagem);
    }
}
