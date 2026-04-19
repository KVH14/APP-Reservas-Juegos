package com.reservas.juegos.service;

import com.reservas.juegos.entities.Tarea;
import com.reservas.juegos.repository.TareaRepository;
import com.reservas.juegos.strategy.EstrategiaOrden;
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

    // Aquí se guarda la estrategia actual
    private EstrategiaOrden estrategia;

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

    // Agrega una tarea a la lista en memoria
    public void agregar(Tarea tarea) {
        tareas.add(tarea);
    }

    // Devuelve la lista completa de tareas
    public List<Tarea> listar() {
        return new ArrayList<>(tareas);
    }

    // Carga tareas desde un archivo CSV
    public void cargarDesdeCSV(String ruta) throws Exception {
        List<Tarea> cargadas = CsvService.cargar(ruta);
        tareas.addAll(cargadas);
    }

    // Exporta las tareas a un archivo CSV
    public void exportarACSV(String ruta) throws Exception {
        CsvService.exportar(tareas, ruta);
    }
}