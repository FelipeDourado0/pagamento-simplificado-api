package br.com.dourado.pagamento.simplificado.api.infra.exceptions;

// 401 não authenticado
public class BasicAuthenticationEntryPointExeption extends BaseExeption {

    public BasicAuthenticationEntryPointExeption(String titulo, String menssagem) {
        super(titulo, menssagem);
    }
}
