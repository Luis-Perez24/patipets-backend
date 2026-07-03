package com.patipets.infrastructure.email;

import com.patipets.core.application.ports.output.EmailSenderPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class SmtpEmailSenderAdapter implements EmailSenderPort {

    private final JavaMailSender mailSender;
    private final String remitente;

    public SmtpEmailSenderAdapter(JavaMailSender mailSender,
                                   @Value("${notificaciones.email.remitente:no-reply@patipets.cl}") String remitente) {
        this.mailSender = mailSender;
        this.remitente = remitente;
    }

    @Override
    public void enviar(String destinatario, String asunto, String cuerpo) {
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setFrom(remitente);
        mensaje.setTo(destinatario);
        mensaje.setSubject(asunto);
        mensaje.setText(cuerpo);
        mailSender.send(mensaje);
    }
}
