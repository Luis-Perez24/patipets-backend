package com.patipets.infrastructure.config;

import com.patipets.core.application.ports.output.ImageStoragePort;
import com.patipets.core.application.ports.output.PasswordEncoderPort;
import com.patipets.core.application.services.AuthService;
import com.patipets.core.application.services.ConsultarAdminDashboardService;
import com.patipets.core.application.services.ConsultarCatalogoPublicoService;
import com.patipets.core.application.services.ConsultarEstadisticasDashboardService;
import com.patipets.core.application.services.ConsultarMapaRefugiosService;
import com.patipets.core.application.services.GestionAlertaService;
import com.patipets.core.application.services.GestionAnimalService;
import com.patipets.core.application.services.GestionRefugioService;
import com.patipets.core.application.services.GestionUsuarioService;
import com.patipets.core.application.useCase.AuthUseCase;
import com.patipets.core.application.useCase.ConsultarAdminDashboardUseCase;
import com.patipets.core.application.useCase.ConsultarCatalogoPublicoUseCase;
import com.patipets.core.application.useCase.ConsultarEstadisticasDashboardUseCase;
import com.patipets.core.application.useCase.ConsultarMapaRefugiosUseCase;
import com.patipets.core.application.useCase.GestionAlertaUseCase;
import com.patipets.core.application.useCase.GestionAnimalUseCase;
import com.patipets.core.application.useCase.GestionRefugioUseCase;
import com.patipets.core.application.useCase.GestionUsuarioUseCase;
import com.patipets.infrastructure.persistence.adapter.AlertaRepositoryAdapter;
import com.patipets.infrastructure.persistence.adapter.AnimalRepositoryAdapter;
import com.patipets.infrastructure.persistence.adapter.EstadisticaRepositoryAdapter;
import com.patipets.infrastructure.persistence.adapter.RefugioRepositoryAdapter;
import com.patipets.infrastructure.persistence.adapter.SolicitudAdopcionRepositoryAdapter;
import com.patipets.infrastructure.persistence.adapter.UsuarioRepositoryAdapter;
import com.patipets.infrastructure.persistence.repository.AlertaJpaRepository;
import com.patipets.infrastructure.persistence.repository.AnimalJpaRepository;
import com.patipets.infrastructure.persistence.repository.RefugioJpaRepository;
import com.patipets.infrastructure.persistence.repository.SolicitudAdopcionJpaRepository;
import com.patipets.infrastructure.persistence.repository.UsuarioJpaRepository;
import com.patipets.infrastructure.persistence.repository.UsuarioRefugioJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    private final AnimalJpaRepository animalJpaRepository;
    private final RefugioJpaRepository refugioJpaRepository;
    private final UsuarioJpaRepository usuarioJpaRepository;
    private final SolicitudAdopcionJpaRepository solicitudJpaRepository;
    private final AlertaJpaRepository alertaJpaRepository;
    private final UsuarioRefugioJpaRepository usuarioRefugioJpaRepository;

    public UseCaseConfig(AnimalJpaRepository animalJpaRepository,
                          RefugioJpaRepository refugioJpaRepository,
                          UsuarioJpaRepository usuarioJpaRepository,
                          SolicitudAdopcionJpaRepository solicitudJpaRepository,
                          AlertaJpaRepository alertaJpaRepository,
                          UsuarioRefugioJpaRepository usuarioRefugioJpaRepository) {
        this.animalJpaRepository = animalJpaRepository;
        this.refugioJpaRepository = refugioJpaRepository;
        this.usuarioJpaRepository = usuarioJpaRepository;
        this.solicitudJpaRepository = solicitudJpaRepository;
        this.alertaJpaRepository = alertaJpaRepository;
        this.usuarioRefugioJpaRepository = usuarioRefugioJpaRepository;
    }

    @Bean
    public ConsultarCatalogoPublicoUseCase consultarCatalogoPublicoUseCase() {
        return new ConsultarCatalogoPublicoService(new AnimalRepositoryAdapter(animalJpaRepository));
    }

    @Bean
    public ConsultarMapaRefugiosUseCase consultarMapaRefugiosUseCase() {
        return new ConsultarMapaRefugiosService(
                new RefugioRepositoryAdapter(refugioJpaRepository, usuarioRefugioJpaRepository));
    }

    @Bean
    public ConsultarEstadisticasDashboardUseCase consultarEstadisticasDashboardUseCase() {
        return new ConsultarEstadisticasDashboardService(
                new EstadisticaRepositoryAdapter(animalJpaRepository, refugioJpaRepository,
                        solicitudJpaRepository, usuarioJpaRepository));
    }

    @Bean
    public ConsultarAdminDashboardUseCase consultarAdminDashboardUseCase() {
        return new ConsultarAdminDashboardService(
                new EstadisticaRepositoryAdapter(animalJpaRepository, refugioJpaRepository,
                        solicitudJpaRepository, usuarioJpaRepository));
    }

    @Bean
    public AuthUseCase authUseCase(PasswordEncoderPort passwordEncoder) {
        return new AuthService(
                new UsuarioRepositoryAdapter(usuarioJpaRepository), passwordEncoder);
    }

    @Bean
    public GestionRefugioUseCase gestionRefugioUseCase() {
        return new GestionRefugioService(
                new RefugioRepositoryAdapter(refugioJpaRepository, usuarioRefugioJpaRepository));
    }

    @Bean
    public GestionAnimalUseCase gestionAnimalUseCase(ImageStoragePort imageStoragePort) {
        return new GestionAnimalService(
                new AnimalRepositoryAdapter(animalJpaRepository),
                new SolicitudAdopcionRepositoryAdapter(solicitudJpaRepository),
                new RefugioRepositoryAdapter(refugioJpaRepository, usuarioRefugioJpaRepository),
                imageStoragePort);
    }

    @Bean
    public GestionUsuarioUseCase gestionUsuarioUseCase() {
        return new GestionUsuarioService(new UsuarioRepositoryAdapter(usuarioJpaRepository));
    }

    @Bean
    public GestionAlertaUseCase gestionAlertaUseCase() {
        return new GestionAlertaService(new AlertaRepositoryAdapter(alertaJpaRepository));
    }
}
