package com.reservas.juegos.controller;

import com.reservas.juegos.dto.TareaDTO;
import com.reservas.juegos.entities.Tarea;
import com.reservas.juegos.factory.TareaFactory;
import com.reservas.juegos.service.TareaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tareas")
public class TareaController {

    private final TareaService service;

    public TareaController(TareaService service) {
        this.service = service;
    }


    @GetMapping
    public List<Tarea> listar() {
        return service.listar();
    }


    @PostMapping
    public Tarea crear(@RequestBody TareaDTO dto) {
        Tarea tarea = new Tarea(dto.getNombre(), dto.getPrioridad());
        service.agregar(tarea);
        return tarea; // Spring lo convierte en JSON automáticamente
    }

    @PostMapping("/factory")
    public Tarea crearConFactory(@RequestParam String tipo, @RequestParam String nombre) {
        Tarea tarea = TareaFactory.crear(tipo, nombre);
        service.agregar(tarea);
        return tarea;
    }
}
