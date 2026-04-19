package com.reservas.juegos.observer;

import com.reservas.juegos.entities.Tarea;

public interface TareaObserver {

    void onTareaAgregada(Tarea tarea);
}
