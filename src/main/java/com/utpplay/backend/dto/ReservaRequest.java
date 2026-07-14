package com.utpplay.backend.dto;

import java.time.LocalDate;

import java.util.List;
import com.utpplay.backend.dto.ImplementoSeleccionDTO;

public class ReservaRequest {
    private String studentId;
    private String cancha;
    private String deporte;
    private LocalDate fecha;
    private String horario;
    private int capacidad;

    private List<ImplementoSeleccionDTO> implementos;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getCancha() {
        return cancha;
    }

    public void setCancha(String cancha) {
        this.cancha = cancha;
    }

    public String getDeporte() {
        return deporte;
    }

    public void setDeporte(String deporte) {
        this.deporte = deporte;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public List<ImplementoSeleccionDTO> getImplementos() {
        return implementos;
    }

    public void setImplementos(List<ImplementoSeleccionDTO> implementos) {
        this.implementos = implementos;
    }
}