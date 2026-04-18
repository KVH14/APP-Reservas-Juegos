package com.reservas.juegos.service;

import com.reservas.juegos.entities.Tarea;
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
