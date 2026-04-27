package com.reservas.juegos.controller;

import com.reservas.juegos.dto.ReservaDTO;
import com.reservas.juegos.entities.Reserva;
import com.reservas.juegos.service.ReservaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservas")
public class ReservaController {
    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    // Crear una reserva (con usuario, producto y fecha)
    @PostMapping("/crear")
    public ResponseEntity<?> crear(@RequestBody ReservaDTO dto) {
        try {
            Reserva reserva = reservaService.crearReserva(dto);
            return ResponseEntity.ok(reserva);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Listar todas las reservas
    @GetMapping
    public ResponseEntity<List<Reserva>> listar() {
        return ResponseEntity.ok(reservaService.listarReservas());
    }

    // Obtener una reserva por ID
    @GetMapping("/{id}")
    public ResponseEntity<Reserva> obtener(@PathVariable Long id) {
        Reserva reserva = reservaService.obtenerReserva(id);
        return reserva != null ? ResponseEntity.ok(reserva) : ResponseEntity.notFound().build();
    }
}
