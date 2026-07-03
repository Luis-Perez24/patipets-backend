package com.patipets.infrastructure.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "usuario_refugio")
public class UsuarioRefugioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(name = "refugio_id", nullable = false)
    private Long refugioId;

    public UsuarioRefugioEntity() {}

    public UsuarioRefugioEntity(Long usuarioId, Long refugioId) {
        this.usuarioId = usuarioId;
        this.refugioId = refugioId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    public Long getRefugioId() { return refugioId; }
    public void setRefugioId(Long refugioId) { this.refugioId = refugioId; }
}
