package com.patipets.infrastructure.security;

import com.patipets.core.application.useCase.AuthUseCase;
import com.patipets.core.domain.models.Usuario;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;
    private final AuthUseCase authUseCase;

    public JwtAuthenticationFilter(JwtTokenProvider tokenProvider, AuthUseCase authUseCase) {
        this.tokenProvider = tokenProvider;
        this.authUseCase = authUseCase;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = extractToken(request);

        if (StringUtils.hasText(token) && tokenProvider.validateToken(token)) {
            Long userId = tokenProvider.getUserIdFromToken(token);
            Usuario usuario = authUseCase.obtenerPorId(userId).orElse(null);

            if (usuario != null && usuario.isActivo()) {
                request.setAttribute("usuarioId", usuario.getId());
                List<SimpleGrantedAuthority> authorities = List.of(
                        new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name())
                );
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(usuario, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }
}
