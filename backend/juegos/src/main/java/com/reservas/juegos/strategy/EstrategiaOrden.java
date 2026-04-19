package com.reservas.juegos.strategy;

import java.util.List;
import com.reservas.juegos.entities.Tarea;

// Interfaz del patrón Strategy
// Define el método que todas las estrategias deben implementar
public interface EstrategiaOrden {

    // Método para ordenar una lista de tareas
    void ordenar(List<Tarea> tareas);
}