package com.reservas.juegos.factory;

import com.reservas.juegos.entities.*;

public class TareaFactory {
    public static Tarea crear(String tipo, String nombre) {
        if ("urgente".equalsIgnoreCase(tipo)) {
            return new TareaUrgente(nombre);
        }
        return new TareaNormal(nombre);
    }
}
