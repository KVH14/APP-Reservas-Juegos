package com.reservas.juegos.service;

import java.util.List;
import com.reservas.juegos.entities.Tarea;
import com.reservas.juegos.strategy.EstrategiaOrden;

public class TareaService {

    private EstrategiaOrden estrategia;

    public void setEstrategia(EstrategiaOrden estrategia) {
        this.estrategia = estrategia;
    }

    public List<Tarea> ordenarTareas(List<Tarea> tareas) {
        if (estrategia != null) {
            estrategia.ordenar(tareas);
        }
        return tareas;
    }
}