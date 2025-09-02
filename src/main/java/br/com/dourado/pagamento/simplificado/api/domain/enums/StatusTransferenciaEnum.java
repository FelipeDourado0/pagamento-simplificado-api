package br.com.dourado.pagamento.simplificado.api.domain.enums;

public enum StatusTransferenciaEnum {
    EFETUADA("efetuada"),
    RECUSADA("recusada");

    private String status;

    StatusTransferenciaEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
