package com.utpplay.backend.dto;

public class UsuarioBusquedaDTO {
    private String studentId;
    private String nombre;
    private String facultad;
    private String deporteFavorito;
    private String nivelHabilidad;
    private boolean verificado;

    public UsuarioBusquedaDTO(String studentId, String nombre, String facultad,
            String deporteFavorito, String nivelHabilidad, boolean verificado) {
        this.studentId = studentId;
        this.nombre = nombre;
        this.facultad = facultad;
        this.deporteFavorito = deporteFavorito;
        this.nivelHabilidad = nivelHabilidad;
        this.verificado = verificado;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getNombre() {
        return nombre;
    }

    public String getFacultad() {
        return facultad;
    }

    public String getDeporteFavorito() {
        return deporteFavorito;
    }

    public String getNivelHabilidad() {
        return nivelHabilidad;
    }

    public boolean isVerificado() {
        return verificado;
    }
}