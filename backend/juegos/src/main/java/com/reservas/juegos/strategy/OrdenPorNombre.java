package com.reservas.juegos.strategy;

import java.util.List;
import com.reservas.juegos.entities.Tarea;

// Estrategia concreta: ordena las tareas alfabéticamente
public class OrdenPorNombre implements EstrategiaOrden {

    @Override
    public void ordenar(List<Tarea> tareas) {

        // Ordena por nombre usando orden alfabético
        tareas.sort((t1, t2) -> t1.getNombre().compareTo(t2.getNombre()));
    }
}