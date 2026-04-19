package com.reservas.juegos.entities;

public class TareaUrgente extends Tarea {
    public TareaUrgente(String nombre) {
        super(nombre, 10); // prioridad alta
    }
}
