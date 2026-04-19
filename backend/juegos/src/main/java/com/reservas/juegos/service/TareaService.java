package com.reservas.juegos.service;

import com.reservas.juegos.entities.Tarea;
import com.reservas.juegos.repository.TareaRepository;
import com.reservas.juegos.strategy.EstrategiaOrden;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// Contiene la lógica de negocio
@Service
public class TareaService {

    @Autowired
    private TareaRepository repository;

    // Aquí se guarda la estrategia actual
    private EstrategiaOrden estrategia;

    // Permite cambiar la estrategia en tiempo de ejecución (clave del patrón)
    public void setEstrategia(EstrategiaOrden estrategia) {
        this.estrategia = estrategia;
    }

    // Guarda una tarea
    public void guardar(Tarea tarea) {
        repository.guardar(tarea);
    }

    // Obtiene las tareas y aplica la estrategia seleccionada
    public List<Tarea> obtenerTareasOrdenadas() {

        List<Tarea> tareas = repository.obtenerTodas();

        // Si hay estrategia, se aplica
        if (estrategia != null) {
            estrategia.ordenar(tareas);
        }

        return tareas;
    }
}