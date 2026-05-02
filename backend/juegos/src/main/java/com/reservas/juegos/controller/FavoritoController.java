package com.reservas.juegos.controller;

import com.reservas.juegos.dto.FavoritoDTO;
import com.reservas.juegos.entities.Favorito;
import com.reservas.juegos.service.FavoritoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favoritos")
@CrossOrigin(origins = "*")
public class FavoritoController {
    private final FavoritoService favoritoService;

    public FavoritoController(FavoritoService favoritoService) {
        this.favoritoService = favoritoService;
    }

    @PostMapping("/crear")
    public ResponseEntity<?> crear(@RequestBody FavoritoDTO dto) {
        try {
            Favorito favorito = favoritoService.crearFavorito(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(favorito);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(java.util.Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<Favorito>> listar() {
        return ResponseEntity.ok(favoritoService.listarFavoritos());
    }
}
