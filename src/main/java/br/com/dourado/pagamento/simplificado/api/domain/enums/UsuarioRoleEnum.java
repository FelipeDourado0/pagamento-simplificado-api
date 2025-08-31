package br.com.dourado.pagamento.simplificado.api.domain.enums;

public enum UsuarioRoleEnum {
    LOJISTA("lojista"),
    CLIENTE("cliente");

    private String role;

    UsuarioRoleEnum(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
