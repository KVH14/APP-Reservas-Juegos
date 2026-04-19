package com.reservas.juegos.dto;

public class TareaDTO {
    private String nombre;
    private int prioridad;

    // Constructor vacío (necesario para que Spring pueda deserializar JSON)
    public TareaDTO() {}

    public TareaDTO(String nombre, int prioridad) {
        this.nombre = nombre;
        this.prioridad = prioridad;
    }

    public String getNombre() {
        return nombre;
    }

    public int getPrioridad() {
        return prioridad;
    }

    @Override
    public String toString() {
        return nombre + " - Prioridad: " + prioridad;
    }
}
