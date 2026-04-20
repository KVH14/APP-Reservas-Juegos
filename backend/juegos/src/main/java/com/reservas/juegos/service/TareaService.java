package com.reservas.juegos.service;

import com.reservas.juegos.entities.Tarea;
import com.reservas.juegos.observer.TareaObserver;
import com.reservas.juegos.repository.TareaRepository;
import com.reservas.juegos.strategy.EstrategiaOrden;
import com.reservas.juegos.observer.LogObserver;
import com.reservas.juegos.observer.AlertaUrgentesObserver;
import com.reservas.juegos.observer.CsvObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

// Contiene la lógica de negocio
@Service
public class TareaService {

    @Autowired(required = false)
    private TareaRepository repository;

    // Lista en memoria para compatibilidad con MenuConsola
    private final List<Tarea> tareas = new ArrayList<>();

    // Lista de observers que escuchan cuando se agrega una tarea
    private final List<TareaObserver> observers = new ArrayList<>();

    // Aquí se guarda la estrategia actual
    private EstrategiaOrden estrategia;

    // Constructor: inicializa los observers
    public TareaService() {
        agregarObserver(new LogObserver());
        agregarObserver(new AlertaUrgentesObserver());
        agregarObserver(new CsvObserver());
    }

    // Agrega un observer a la lista
    public void agregarObserver(TareaObserver observer) {
        observers.add(observer);
    }

    // Notifica a todos los observers cuando se agrega una tarea
    private void notificar(Tarea tarea) {
        observers.forEach(o -> o.onTareaAgregada(tarea));
    }

    // Permite cambiar la estrategia en tiempo de ejecución (clave del patrón)
    public void setEstrategia(EstrategiaOrden estrategia) {
        this.estrategia = estrategia;
    }

    // Guarda una tarea en el repository
    public void guardar(Tarea tarea) {
        if (repository != null) {
            repository.guardar(tarea);
        }
    }

    // Obtiene las tareas y aplica la estrategia seleccionada
    public List<Tarea> obtenerTareasOrdenadas() {
        List<Tarea> resultado;
        
        if (repository != null) {
            resultado = repository.obtenerTodas();
        } else {
            resultado = new ArrayList<>(tareas);
        }

        // Si hay estrategia, se aplica
        if (estrategia != null) {
            estrategia.ordenar(resultado);
        }

        return resultado;
    }

    // -------------------------------------------------------
    // Métodos compatibles con MenuConsola (lista en memoria)
    // -------------------------------------------------------

    // Agrega una tarea a la lista en memoria y notifica a los observers
    public void agregar(Tarea tarea) {
        tareas.add(tarea);
        notificar(tarea);
    }

    // Devuelve la lista completa de tareas
    public List<Tarea> listar() {
        return new ArrayList<>(tareas);
    }

    // Carga tareas desde un archivo CSV y notifica por cada una
    public void cargarDesdeCSV(String ruta) throws Exception {
        List<Tarea> cargadas = CsvService.cargar(ruta);
        for (Tarea tarea : cargadas) {
            tareas.add(tarea);
            notificar(tarea);
        }
    }

    // Exporta las tareas a un archivo CSV
    public void exportarACSV(String ruta) throws Exception {
        CsvService.exportar(tareas, ruta);
    }
}