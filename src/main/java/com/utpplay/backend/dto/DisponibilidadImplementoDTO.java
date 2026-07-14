package com.utpplay.backend.dto;

public class DisponibilidadImplementoDTO {
    private Long id;
    private String tipo;
    private String deporte;
    private int stockTotal;
    private int disponible;

    public DisponibilidadImplementoDTO(Long id, String tipo, String deporte, int stockTotal, int disponible) {
        this.id = id;
        this.tipo = tipo;
        this.deporte = deporte;
        this.stockTotal = stockTotal;
        this.disponible = disponible;
    }

    public Long getId() {
        return id;
    }

    public String getTipo() {
        return tipo;
    }

    public String getDeporte() {
        return deporte;
    }

    public int getStockTotal() {
        return stockTotal;
    }

    public int getDisponible() {
        return disponible;
    }
}