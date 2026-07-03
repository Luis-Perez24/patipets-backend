package com.patipets.infrastructure.security;

import com.patipets.core.application.useCase.GestionRefugioUseCase;
import com.patipets.core.domain.enums.Rol;
import com.patipets.core.domain.models.Usuario;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

@Component
public class RefugioAccessGuard {

    private final GestionRefugioUseCase gestionRefugioUseCase;

    public RefugioAccessGuard(GestionRefugioUseCase gestionRefugioUseCase) {
        this.gestionRefugioUseCase = gestionRefugioUseCase;
    }

    public void verificar(Usuario usuario, Long refugioId) {
        if (usuario.getRol() == Rol.ADMIN) {
            return;
        }
        if (!gestionRefugioUseCase.perteneceAUsuario(refugioId, usuario.getId())) {
            throw new AccessDeniedException("No tienes acceso a este refugio");
        }
    }
}
