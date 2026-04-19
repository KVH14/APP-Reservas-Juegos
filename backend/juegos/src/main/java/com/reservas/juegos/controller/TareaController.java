package com.reservas.juegos.controller;

import com.reservas.juegos.entities.Tarea;
import com.reservas.juegos.service.TareaService;
import com.reservas.juegos.strategy.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Controlador REST (exposición de endpoints)
@RestController
@RequestMapping("/tareas")
public class TareaController {

    @Autowired
    private TareaService service;

    // Endpoint para crear una tarea (POST)
    @PostMapping
    public void crearTarea(@RequestBody Tarea tarea) {

        // Recibe JSON y lo guarda
        service.guardar(tarea);
    }

    // Endpoint para obtener tareas ordenadas (GET)
    @GetMapping
    public List<Tarea> obtenerTareas(@RequestParam String tipoOrden) {

        // Selección dinámica de estrategia (aquí ocurre el Strategy)
        if (tipoOrden.equalsIgnoreCase("prioridad")) {
            service.setEstrategia(new OrdenPorPrioridad());
        } else {
            service.setEstrategia(new OrdenPorNombre());
        }

        // Retorna la lista ya ordenada
        return service.obtenerTareasOrdenadas();
    }
}