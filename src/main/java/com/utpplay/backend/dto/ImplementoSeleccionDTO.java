package com.utpplay.backend.dto;

public class ImplementoSeleccionDTO {
    private String tipo; // PELOTA o CHALECO
    private int cantidad;

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}