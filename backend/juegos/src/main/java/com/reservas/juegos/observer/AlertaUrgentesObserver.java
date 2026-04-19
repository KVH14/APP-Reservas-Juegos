package com.reservas.juegos.observer;

import com.reservas.juegos.entities.Tarea;
public class AlertaUrgentesObserver implements TareaObserver {
    @Override
    public void onTareaAgregada(Tarea tarea) {
        if (tarea.getPrioridad() >= 10) {
            System.out.println("[ALERTA] ¡Tarea URGENTE detectada: "
                    + tarea.getNombre() + "!");
        }
    }
}