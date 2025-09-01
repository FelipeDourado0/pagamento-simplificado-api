package br.com.dourado.pagamento.simplificado.api.infra.exceptions;

//erro 403 - sem autorização
public class AccessDeniedException extends BaseExeption {
    protected AccessDeniedException(String titulo, String menssagem) {
        super(titulo, menssagem);
    }
}
