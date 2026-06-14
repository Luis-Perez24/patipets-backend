package com.patipets.infrastructure.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponseDTO<T> {

    private boolean success;
    private String mensaje;
    private T datos;
    private List<String> errores;

    private ApiResponseDTO(boolean success, String mensaje, T datos, List<String> errores) {
        this.success = success;
        this.mensaje = mensaje;
        this.datos = datos;
        this.errores = errores;
    }

    public static <T> ApiResponseDTO<T> ok(T datos) {
        return new ApiResponseDTO<>(true, "Operación exitosa", datos, null);
    }

    public static <T> ApiResponseDTO<T> ok(String mensaje, T datos) {
        return new ApiResponseDTO<>(true, mensaje, datos, null);
    }

    public static <T> ApiResponseDTO<T> error(String mensaje) {
        return new ApiResponseDTO<>(false, mensaje, null, null);
    }

    public static <T> ApiResponseDTO<T> error(String mensaje, List<String> errores) {
        return new ApiResponseDTO<>(false, mensaje, null, errores);
    }

    public boolean isSuccess() { return success; }
    public String getMensaje() { return mensaje; }
    public T getDatos() { return datos; }
    public List<String> getErrores() { return errores; }
}
