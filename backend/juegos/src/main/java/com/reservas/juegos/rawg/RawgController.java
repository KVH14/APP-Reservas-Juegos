package com.reservas.juegos.rawg;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rawg")
@CrossOrigin(origins = "*")
public class RawgController {

    private final RawgService rawgService;

    public RawgController(RawgService rawgService) {
        this.rawgService = rawgService;
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<RawgJuegoDTO>> buscar(@RequestParam String q) {
        List<RawgJuegoDTO> resultados = rawgService.buscar(q);
        return ResponseEntity.ok(resultados);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RawgJuegoDTO> obtenerPorId(@PathVariable Long id) {
        RawgJuegoDTO juego = rawgService.obtenerPorId(id);
        if (juego == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(juego);
    }
}
