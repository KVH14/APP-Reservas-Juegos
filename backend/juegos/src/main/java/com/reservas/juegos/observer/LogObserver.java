package com.reservas.juegos.observer;

import com.reservas.juegos.entities.Tarea;

public class LogObserver implements TareaObserver{
    @Override
    public void onTareaAgregada(Tarea tarea){
        System.out.println("[LOG] Nueva tarea: "+ tarea.getNombre()
        +" | Prioridad: " + tarea.getPrioridad());
    }
}
