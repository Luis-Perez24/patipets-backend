package com.patipets.core.application.ports.output;

public interface EmailSenderPort {
    void enviar(String destinatario, String asunto, String cuerpo);
}
