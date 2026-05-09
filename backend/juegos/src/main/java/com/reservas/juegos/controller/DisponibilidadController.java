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

    @GetMapping("/{productoId}")
    public ResponseEntity<Boolean> verificar(@PathVariable Long productoId) {
        return ResponseEntity.ok(disponibilidadService.verificarDisponibilidad(productoId));
    }

    @PutMapping("/{productoId}")
    public ResponseEntity<Boolean> cambiar(@PathVariable Long productoId,
                                           @RequestParam boolean disponible) {
        return ResponseEntity.ok(disponibilidadService.cambiarDisponibilidad(productoId, disponible));
    }
}
