package com.reservas.juegos.service;

import com.reservas.juegos.entities.Tarea;
import com.reservas.juegos.observer.TareaObserver;
import com.reservas.juegos.strategy.EstrategiaOrden;
import org.springframework.stereotype.Service;

import com.reservas.juegos.observer.LogObserver;
import com.reservas.juegos.observer.AlertaUrgentesObserver;
import com.reservas.juegos.observer.CsvObserver;

import java.util.ArrayList;
import java.util.List;

@Service
public class TareaService {
    private final List<Tarea> tareas = new ArrayList<>();
    private EstrategiaOrden estrategia;

    private final List<TareaObserver> observers = new ArrayList<>();


    public TareaService() {
        agregarObserver(new LogObserver());
        agregarObserver(new AlertaUrgentesObserver());
        agregarObserver(new CsvObserver());
    }


    public void agregarObserver(TareaObserver observer) {
        observers.add(observer);
    }

    private void notificar(Tarea tarea) {
        observers.forEach(o -> o.onTareaAgregada(tarea));
    }


    public List<Tarea> listar() {
        return tareas;
    }

    public void agregar(Tarea tarea) {
        tareas.add(tarea);
        notificar(tarea);
    }
}
