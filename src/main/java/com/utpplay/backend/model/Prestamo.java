package com.utpplay.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "prestamos")
public class Prestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reserva_id", nullable = false)
    @JsonIgnore // evita recursión infinita reserva -> prestamos -> reserva
    private Reserva reserva;

    @ManyToOne
    @JoinColumn(name = "implemento_id", nullable = false)
    private Implemento implemento;

    @Column(nullable = false)
    private int cantidad;

    @Column(nullable = false)
    private String estado = "PRESTADO"; // PRESTADO, DEVUELTO

    private LocalDateTime creadoEn = LocalDateTime.now();
    private LocalDateTime devueltoEn;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Reserva getReserva() {
        return reserva;
    }

    public void setReserva(Reserva reserva) {
        this.reserva = reserva;
    }

    public Implemento getImplemento() {
        return implemento;
    }

    public void setImplemento(Implemento implemento) {
        this.implemento = implemento;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
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

    public LocalDateTime getDevueltoEn() {
        return devueltoEn;
    }

    public void setDevueltoEn(LocalDateTime devueltoEn) {
        this.devueltoEn = devueltoEn;
    }
}