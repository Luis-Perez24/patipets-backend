package com.patipets.infrastructure.web.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthResponseDTO {

    private String token;
    private UsuarioResponseDTO usuario;

    private AuthResponseDTO() {}

    public AuthResponseDTO(String token, UsuarioResponseDTO usuario) {
        this.token = token;
        this.usuario = usuario;
    }

    public String getToken() { return token; }
    public UsuarioResponseDTO getUsuario() { return usuario; }

    @JsonProperty("token_type")
    public String getTokenType() {
        return "Bearer";
    }
}
