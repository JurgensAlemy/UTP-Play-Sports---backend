package com.utpplay.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "mensajes_leidos", uniqueConstraints = @UniqueConstraint(columnNames = { "studentId", "conexion_id" }))
public class MensajeLeido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String studentId;

    @ManyToOne
    @JoinColumn(name = "conexion_id", nullable = false)
    private Conexion conexion;

    @Column(nullable = false)
    private Long ultimoMensajeVistoId;

    private LocalDateTime actualizadoEn = LocalDateTime.now();

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

    public Conexion getConexion() {
        return conexion;
    }

    public void setConexion(Conexion conexion) {
        this.conexion = conexion;
    }

    public Long getUltimoMensajeVistoId() {
        return ultimoMensajeVistoId;
    }

    public void setUltimoMensajeVistoId(Long id) {
        this.ultimoMensajeVistoId = id;
    }

    public LocalDateTime getActualizadoEn() {
        return actualizadoEn;
    }

    public void setActualizadoEn(LocalDateTime a) {
        this.actualizadoEn = a;
    }
}