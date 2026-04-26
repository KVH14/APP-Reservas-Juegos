package com.reservas.juegos.controller;

import com.reservas.juegos.dto.TareaDTO;
import com.reservas.juegos.entities.Tarea;
import com.reservas.juegos.factory.TareaFactory;
import com.reservas.juegos.service.TareaService;
import com.reservas.juegos.strategy.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/strategy/tareas")
public class StrategyTareaController {

    @Autowired
    private TareaService service;

    @PostMapping
    public void crearTarea(@RequestBody TareaDTO tareaDTO) {
        // Convierte el DTO a entidad usando Factory con la prioridad real
        Tarea tarea = TareaFactory.crear(
            tareaDTO.getPrioridad() >= 10 ? "urgente" : "normal",
            tareaDTO.getNombre(),
            tareaDTO.getPrioridad()
        );
        tarea.setPrioridad(tareaDTO.getPrioridad());
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

    @GetMapping("/exportar")
    public String exportar() throws Exception {
        service.exportarACSV("tareas.csv");
        return "Exportado correctamente";
    }

    @GetMapping("/cargar")
    public String cargar() throws Exception {
        service.cargarDesdeCSV("tareas.csv");
        return "Cargado correctamente";
    }
}
