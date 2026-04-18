package com.reservas.juegos.strategy;

import java.util.List;
import com.reservas.juegos.entities.Tarea;

public interface EstrategiaOrden {
    void ordenar(List<Tarea> tareas);
}