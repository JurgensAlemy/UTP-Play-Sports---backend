package com.utpplay.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notificaciones_vistas", uniqueConstraints = @UniqueConstraint(columnNames = { "studentId",
        "solicitud_id" }))
public class NotificacionVista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String studentId;

    @ManyToOne
    @JoinColumn(name = "solicitud_id", nullable = false)
    private SolicitudMatchmaking solicitud;

    private LocalDateTime vistoEn = LocalDateTime.now();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public SolicitudMatchmaking getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(SolicitudMatchmaking solicitud) {
        this.solicitud = solicitud;
    }

    public LocalDateTime getVistoEn() {
        return vistoEn;
    }

    public void setVistoEn(LocalDateTime vistoEn) {
        this.vistoEn = vistoEn;
    }
}