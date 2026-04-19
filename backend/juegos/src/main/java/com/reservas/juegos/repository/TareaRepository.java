package com.reservas.juegos.repository;

import com.reservas.juegos.entities.Tarea;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

// Simula una base de datos en memoria
@Repository
public class TareaRepository {

    // Lista donde se guardan las tareas
    private List<Tarea> tareas = new ArrayList<>();

    // Retorna todas las tareas
    public List<Tarea> obtenerTodas() {
        return tareas;
    }

    // Guarda una nueva tarea en la lista
    public void guardar(Tarea tarea) {
        tareas.add(tarea);
    }
}