package com.reservas.juegos.entities;

public class TareaNormal extends Tarea {
    public TareaNormal(String nombre) {
        super(nombre, 1); // prioridad baja por defecto
    }

    // Constructor que acepta prioridad personalizada
    public TareaNormal(String nombre, int prioridad) {
        super(nombre, prioridad);
    }
}
