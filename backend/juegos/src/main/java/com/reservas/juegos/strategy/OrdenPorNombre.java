package com.reservas.juegos.strategy;

import java.util.List;
import com.reservas.juegos.entities.Tarea;

public class OrdenPorNombre implements EstrategiaOrden {

    @Override
    public void ordenar(List<Tarea> tareas) {
        tareas.sort((t1, t2) -> t1.getNombre().compareTo(t2.getNombre()));
    }
}