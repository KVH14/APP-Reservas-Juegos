package com.reservas.juegos.controller;

import com.reservas.juegos.dto.CaracteristicaDTO;
import com.reservas.juegos.entities.Caracteristica;
import com.reservas.juegos.service.CaracteristicaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * #17 Administrar característica de producto
 *
 * Endpoints:
 *   GET    /api/caracteristicas              → listar todas
 *   GET    /api/caracteristicas/{id}         → buscar por id
 *   POST   /api/caracteristicas              → agregar característica
 *   PUT    /api/caracteristicas/{id}         → actualizar característica
 *   DELETE /api/caracteristicas/{id}         → eliminar característica
 */
@RestController
@RequestMapping("/api/caracteristicas")
@CrossOrigin(origins = "*")
public class CaracteristicaController {

    @Autowired
    private CaracteristicaService caracteristicaService;

    @GetMapping
    public ResponseEntity<List<Caracteristica>> listarTodas() {
        return ResponseEntity.ok(caracteristicaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Caracteristica> buscarPorId(@PathVariable Long id) {
        return caracteristicaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> agregar(@RequestBody CaracteristicaDTO dto) {
        if (dto.getClave() == null || dto.getClave().isBlank()
                || dto.getValor() == null || dto.getValor().isBlank()) {
            return ResponseEntity.badRequest().body("Clave y valor son obligatorios.");
        }
        return caracteristicaService.crear(dto)
                .map(c -> ResponseEntity.status(HttpStatus.CREATED).body((Object) c))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Producto no encontrado."));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody CaracteristicaDTO dto) {
        return caracteristicaService.actualizar(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        return caracteristicaService.eliminar(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
