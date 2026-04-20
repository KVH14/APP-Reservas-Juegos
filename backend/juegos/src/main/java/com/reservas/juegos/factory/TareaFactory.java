package com.reservas.juegos.factory;

import com.reservas.juegos.entities.*;

public class TareaFactory {
    public static Tarea crear(String tipo, String nombre) {
        if ("urgente".equalsIgnoreCase(tipo)) {
            return new TareaUrgente(nombre);
        }
        return new TareaNormal(nombre);
    }

    // Nuevo método que acepta prioridad personalizada
    public static Tarea crear(String tipo, String nombre, int prioridad) {
        if ("urgente".equalsIgnoreCase(tipo)) {
            return new TareaUrgente(nombre, prioridad);
        }
        return new TareaNormal(nombre, prioridad);
    }
}
