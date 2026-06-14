package com.patipets.infrastructure.security;

import com.patipets.core.application.ports.output.PasswordEncoderPort;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BCryptPasswordEncoderAdapter implements PasswordEncoderPort {

    private final PasswordEncoder springEncoder;

    public BCryptPasswordEncoderAdapter(@Lazy PasswordEncoder springEncoder) {
        this.springEncoder = springEncoder;
    }

    @Override
    public String encode(String rawPassword) {
        return springEncoder.encode(rawPassword);
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return springEncoder.matches(rawPassword, encodedPassword);
    }
}
