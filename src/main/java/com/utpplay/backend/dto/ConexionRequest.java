package com.utpplay.backend.dto;

public class ConexionRequest {
    private Long solicitudId;
    private String studentId;
    private String mensaje;

    public Long getSolicitudId() {
        return solicitudId;
    }

    public void setSolicitudId(Long solicitudId) {
        this.solicitudId = solicitudId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}