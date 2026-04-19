package com.reservas.juegos.controller;

import com.reservas.juegos.entities.Tarea;
import com.reservas.juegos.service.TareaService;
import com.reservas.juegos.strategy.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/strategy/tareas") // 👈 ruta diferente
public class StrategyTareaController {

    @Autowired
    private TareaService service;

    @PostMapping
    public void crearTarea(@RequestBody Tarea tarea) {
        service.guardar(tarea);
    }

    @GetMapping
    public List<Tarea> obtenerTareas(@RequestParam String tipoOrden) {

        if (tipoOrden.equalsIgnoreCase("prioridad")) {
            service.setEstrategia(new OrdenPorPrioridad());
        } else {
            service.setEstrategia(new OrdenPorNombre());
        }

        return service.obtenerTareasOrdenadas();
    }
}