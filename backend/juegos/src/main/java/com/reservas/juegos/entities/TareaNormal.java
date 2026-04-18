package com.reservas.juegos.entities;

public class TareaNormal extends Tarea {
    public TareaNormal(String nombre) {
        super(nombre, 1); // prioridad baja
    }
}
