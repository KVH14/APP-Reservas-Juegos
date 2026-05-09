package com.reservas.juegos.controller;

import com.reservas.juegos.service.DisponibilidadService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/disponibilidad")
public class DisponibilidadController {
    private final DisponibilidadService disponibilidadService;

    public DisponibilidadController(DisponibilidadService disponibilidadService) {
        this.disponibilidadService = disponibilidadService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> verificar(@PathVariable Long id) {
        boolean disponible = disponibilidadService.verificarDisponibilidad(id);
        return ResponseEntity.ok(disponible ? "Disponible" : "No disponible");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> cambiar(@PathVariable Long id, @RequestParam boolean disponible) {
        boolean actualizado = disponibilidadService.cambiarDisponibilidad(id, disponible);
        if (actualizado) {
            return ResponseEntity.ok("Disponibilidad actualizada a: " + (disponible ? "Disponible" : "No disponible"));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}