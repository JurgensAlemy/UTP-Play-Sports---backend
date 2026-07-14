package com.utpplay.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "implementos")
public class Implemento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tipo; // PELOTA, CHALECO

    @Column(nullable = false)
    private String deporte; // Fútbol, Básquetbol, Vóley, Tenis o "General" (chalecos)

    @Column(nullable = false)
    private int stockTotal;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDeporte() {
        return deporte;
    }

    public void setDeporte(String deporte) {
        this.deporte = deporte;
    }

    public int getStockTotal() {
        return stockTotal;
    }

    public void setStockTotal(int stockTotal) {
        this.stockTotal = stockTotal;
    }
}