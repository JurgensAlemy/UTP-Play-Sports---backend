package com.utpplay.backend.dto;

public class RespuestaConexionRequest {
    private String studentId; // quien responde (debe ser el dueño de la solicitud)
    private String estado; // ACEPTADA o RECHAZADA

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}