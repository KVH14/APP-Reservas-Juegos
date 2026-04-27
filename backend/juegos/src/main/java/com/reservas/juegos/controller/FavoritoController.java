package com.reservas.juegos.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/favoritos")
@CrossOrigin(origins = "*")
public class FavoritoController {

    // Listar favoritos
    @GetMapping
    public List<String> listarFavoritos() {
        return List.of("FIFA", "Minecraft", "Call of Duty");
    }
}