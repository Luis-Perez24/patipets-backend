package com.patipets.infrastructure.web.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.patipets.core.domain.models.Usuario;

public class UsuarioResponseDTO {

    private Long id;
    private String nombre;
    private String email;
    private String rol;

    @JsonProperty("foto_perfil")
    private String fotoPerfil;

    @JsonProperty("numero_contacto")
    private String numeroContacto;

    private String ubicacion;

    private String region;

    private String comuna;

    private String direccion;

    private String biografia;

    private boolean activo;

    @JsonProperty("fecha_registro")
    private String fechaRegistro;

    @JsonProperty("refugio_id")
    private Long refugioId;

    private UsuarioResponseDTO() {}

    public static UsuarioResponseDTO fromDomain(Usuario usuario) {
        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.id = usuario.getId();
        dto.nombre = usuario.getNombre();
        dto.email = usuario.getEmail();
        dto.rol = usuario.getRol().name();
        dto.fotoPerfil = usuario.getFotoPerfil();
        dto.numeroContacto = usuario.getNumeroContacto();
        dto.ubicacion = usuario.getUbicacion();
        dto.region = usuario.getRegion();
        dto.comuna = usuario.getComuna();
        dto.direccion = usuario.getDireccion();
        dto.biografia = usuario.getBiografia();
        dto.activo = usuario.isActivo();
        dto.fechaRegistro = usuario.getFechaRegistro().toString();
        dto.refugioId = usuario.getRefugioId();
        return dto;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public String getRol() { return rol; }
    public String getFotoPerfil() { return fotoPerfil; }
    public String getNumeroContacto() { return numeroContacto; }
    public String getUbicacion() { return ubicacion; }
    public String getRegion() { return region; }
    public String getComuna() { return comuna; }
    public String getDireccion() { return direccion; }
    public String getBiografia() { return biografia; }
    public boolean isActivo() { return activo; }
    public String getFechaRegistro() { return fechaRegistro; }
    public Long getRefugioId() { return refugioId; }
}
