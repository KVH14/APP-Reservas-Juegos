package com.reservas.juegos.entities;

public class TareaUrgente extends Tarea {
    public TareaUrgente(String nombre) {
        super(nombre, 10); // prioridad alta por defecto
    }

    // Constructor que acepta prioridad personalizada
    public TareaUrgente(String nombre, int prioridad) {
        super(nombre, prioridad);
    }
}
