package com.reservas.juegos.observer;
import com.reservas.juegos.entities.Tarea;

public class CsvObserver implements TareaObserver {
    @Override
    public void onTareaAgregada(Tarea tarea) {
        System.out.println("[CSV] Registrando: "
                + tarea.getNombre() + "," + tarea.getPrioridad());
        // aquí irían las líneas de CsvService para escribir en archivo
    }
}