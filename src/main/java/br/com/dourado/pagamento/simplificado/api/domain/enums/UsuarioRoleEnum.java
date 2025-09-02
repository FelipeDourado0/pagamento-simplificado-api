package br.com.dourado.pagamento.simplificado.api.domain.enums;

public enum UsuarioRoleEnum {
    LOJISTA("ROLE_LOJISTA"),
    CLIENTE("ROLE_CLIENTE");

    private String role;

    UsuarioRoleEnum(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
