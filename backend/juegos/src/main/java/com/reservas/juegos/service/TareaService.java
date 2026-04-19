package com.reservas.juegos.service;

import com.reservas.juegos.entities.Tarea;
import com.reservas.juegos.observer.TareaObserver;
import org.springframework.stereotype.Service;

import com.reservas.juegos.observer.LogObserver;
import com.reservas.juegos.observer.AlertaUrgentesObserver;
import com.reservas.juegos.observer.CsvObserver;

import java.util.ArrayList;
import java.util.List;

@Service
public class TareaService {

    // lista que guarda todas las tareas mientras el programa esta corriendo
    private final List<Tarea> tareas = new ArrayList<>();

    // lista de observers que escuchan cuando se agrega una tarea
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

    // -------------------------------------------------------
    // devuelve la lista completa de tareas
    // -------------------------------------------------------
    public List<Tarea> listar() {
        return tareas;
    }

    // -------------------------------------------------------
    // agrega una tarea nueva a la lista
    // -------------------------------------------------------
    public void agregar(Tarea tarea) {
        tareas.add(tarea);
        notificar(tarea);
    }

    // -------------------------------------------------------
    // lee las tareas desde un archivo CSV y las agrega a la lista
    // -------------------------------------------------------
    public void cargarDesdeCSV(String ruta) throws Exception {
        List<Tarea> cargadas = CsvService.cargar(ruta);
        tareas.addAll(cargadas);
    }

    // -------------------------------------------------------
    // guarda todas las tareas de la lista en un archivo CSV
    // -------------------------------------------------------
    public void exportarACSV(String ruta) throws Exception {
        CsvService.exportar(tareas, ruta);
    }
}
