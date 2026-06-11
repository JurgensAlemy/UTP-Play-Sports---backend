package com.utpplay.backend.dto;

public class LoginResponse {
    private boolean success;
    private String message;
    private String studentId;
    private String nombre;
    private String email;
    private String facultad;
    private boolean verificado;

    public LoginResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public LoginResponse(boolean success, String message, String studentId,
            String nombre, String email, String facultad, boolean verificado) {
        this.success = success;
        this.message = message;
        this.studentId = studentId;
        this.nombre = nombre;
        this.email = email;
        this.facultad = facultad;
        this.verificado = verificado;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFacultad() {
        return facultad;
    }

    public void setFacultad(String facultad) {
        this.facultad = facultad;
    }

    public boolean isVerificado() {
        return verificado;
    }

    public void setVerificado(boolean verificado) {
        this.verificado = verificado;
    }
}