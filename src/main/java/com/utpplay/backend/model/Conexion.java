package com.utpplay.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "conexiones")
public class Conexion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "solicitud_id", nullable = false)
    private SolicitudMatchmaking solicitud;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(length = 500)
    private String mensaje;

    @Column(nullable = false)
    private String estado = "PENDIENTE"; // PENDIENTE, ACEPTADA, RECHAZADA

    private LocalDateTime creadoEn = LocalDateTime.now();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SolicitudMatchmaking getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(SolicitudMatchmaking solicitud) {
        this.solicitud = solicitud;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getCreadoEn() {
        return creadoEn;
    }

    public void setCreadoEn(LocalDateTime creadoEn) {
        this.creadoEn = creadoEn;
    }
}