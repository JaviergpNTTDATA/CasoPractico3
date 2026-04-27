package com.casopractico3.CaPr3.dto;

public class LoginResponseDTO {

    private String token;
    private String tipo = "Bearer";
    private Long expiracion;

    public LoginResponseDTO(String token, Long expiracion) {
        this.token = token;
        this.expiracion = expiracion;
    }

    public String getToken() {
        return token;
    }

    public String getTipo() {
        return tipo;
    }

    public Long getExpiracion() {
        return expiracion;
    }
}