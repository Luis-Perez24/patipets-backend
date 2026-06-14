package com.patipets.core.application.useCase;

import com.patipets.core.domain.models.Refugio;
import java.util.List;

public interface ConsultarMapaRefugiosUseCase {
    List<Refugio> obtenerRefugiosAprobados();
}
