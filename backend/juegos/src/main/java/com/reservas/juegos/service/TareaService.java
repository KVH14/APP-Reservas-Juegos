package com.reservas.juegos.service;

import com.reservas.juegos.entities.Tarea;
import com.reservas.juegos.observer.TareaObserver;
import com.reservas.juegos.strategy.EstrategiaOrden;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TareaService {
    private final List<Tarea> tareas = new ArrayList<>();


    public List<Tarea> listar() {
        return tareas;
    }

    public void agregar(Tarea tarea) {
        tareas.add(tarea);
    }
}
