package com.reservas.juegos.controller;


import com.reservas.juegos.dto.ReservaDTO;
import com.reservas.juegos.entities.Reserva;
import com.reservas.juegos.service.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * #32 – Reservas: Realizar reserva
 *
 * Endpoints:
 *   GET    /api/reservas                      → listar todas
 *   GET    /api/reservas/{id}                 → buscar por id
 *   GET    /api/reservas/email/{email}         → reservas por cliente
 *   GET    /api/reservas/producto/{id}         → reservas por producto
 *   POST   /api/reservas                      → crear reserva
 *   PATCH  /api/reservas/{id}/confirmar        → confirmar reserva
 *   PATCH  /api/reservas/{id}/cancelar         → cancelar reserva
 *   DELETE /api/reservas/{id}                 → eliminar reserva
 */
@RestController
@RequestMapping("/api/reservas")
@CrossOrigin(origins = "*")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @GetMapping
    public ResponseEntity<List<Reserva>> listarTodas() {
        return ResponseEntity.ok(reservaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reserva> buscarPorId(@PathVariable Long id) {
        return reservaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<List<Reserva>> buscarPorEmail(@PathVariable String email) {
        return ResponseEntity.ok(reservaService.buscarPorEmail(email));
    }

    @GetMapping("/producto/{productoId}")
    public ResponseEntity<List<Reserva>> buscarPorProducto(@PathVariable Long productoId) {
        return ResponseEntity.ok(reservaService.buscarPorProducto(productoId));
    }

    // #32 – Realizar reserva
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody ReservaDTO dto) {
        if (dto.getNombreCliente() == null || dto.getNombreCliente().isBlank()) {
            return ResponseEntity.badRequest().body("El nombre del cliente es obligatorio.");
        }
        if (dto.getEmailCliente() == null || dto.getEmailCliente().isBlank()) {
            return ResponseEntity.badRequest().body("El email del cliente es obligatorio.");
        }
        if (dto.getProductoId() == null) {
            return ResponseEntity.badRequest().body("El productoId es obligatorio.");
        }
        if (dto.getFechaReserva() == null) {
            return ResponseEntity.badRequest().body("La fechaReserva es obligatoria.");
        }

        return reservaService.crear(dto)
                .map(r -> ResponseEntity.status(HttpStatus.CREATED).body((Object) r))
                .orElse(ResponseEntity.badRequest()
                        .body("No se pudo crear la reserva. Verifique que el producto exista y tenga stock."));
    }

    @PatchMapping("/{id}/confirmar")
    public ResponseEntity<?> confirmar(@PathVariable Long id) {
        return reservaService.confirmar(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelar(@PathVariable Long id) {
        return reservaService.cancelar(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        return reservaService.eliminar(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}