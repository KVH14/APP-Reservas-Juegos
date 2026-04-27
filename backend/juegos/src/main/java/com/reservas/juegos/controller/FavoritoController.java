package com.reservas.juegos.controller;

import com.reservas.juegos.entities.Favorito;
import com.reservas.juegos.service.FavoritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favoritos")
public class FavoritoController {

    @Autowired
    private FavoritoService favoritoService;

    @PostMapping
    public Favorito marcarFavorito(@RequestParam Long usuarioId, @RequestParam Long productoId) {
        return favoritoService.marcarFavorito(usuarioId, productoId);
    }

    @GetMapping("/{usuarioId}")
    public List<Favorito> listarFavoritos(@PathVariable Long usuarioId) {
        return favoritoService.listarFavoritos(usuarioId);
    }

    @DeleteMapping
    public boolean eliminarFavorito(@RequestParam Long usuarioId, @RequestParam Long productoId) {
        return favoritoService.eliminarFavorito(usuarioId, productoId);
    }
}
