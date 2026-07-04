package com.patipets.infrastructure.web.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegisterRequestDTO {

    @NotBlank(message = "es obligatorio")
    private String nombre;

    @NotBlank(message = "es obligatorio")
    @Email(message = "debe ser un correo electrónico válido (ej: usuario@dominio.com)")
    @Pattern(
        regexp = "^[\\w.%+-]+@[\\w.-]+\\.\\w{2,}$",
        message = "debe tener un dominio válido con extensión (ej: usuario@dominio.com)"
    )
    private String email;

    @NotBlank(message = "es obligatorio")
    @Size(min = 8, max = 100, message = "debe tener entre 8 y 100 caracteres")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>]).+$",
        message = "debe contener al menos una mayúscula, una minúscula, un dígito y un carácter especial"
    )
    private String password;

    @NotBlank(message = "es obligatorio")
    private String numeroContacto;

    @NotBlank(message = "es obligatorio")
    private String region;

    @NotBlank(message = "es obligatorio")
    private String comuna;

    @NotBlank(message = "es obligatorio")
    private String direccion;

    public RegisterRequestDTO() {}

    public RegisterRequestDTO(String nombre, String email, String password, String numeroContacto,
                               String region, String comuna, String direccion) {
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.numeroContacto = numeroContacto;
        this.region = region;
        this.comuna = comuna;
        this.direccion = direccion;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getNumeroContacto() { return numeroContacto; }
    public void setNumeroContacto(String numeroContacto) { this.numeroContacto = numeroContacto; }
    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
    public String getComuna() { return comuna; }
    public void setComuna(String comuna) { this.comuna = comuna; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
}
