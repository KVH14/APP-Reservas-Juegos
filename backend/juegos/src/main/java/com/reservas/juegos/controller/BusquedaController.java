package com.reservas.juegos.controller;

import com.reservas.juegos.entities.Producto;
import com.reservas.juegos.service.ProductoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/busqueda")
public class BusquedaController {

    private final ProductoService productoService;

    public BusquedaController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping("/genero/{genero}")
    public List<Producto> buscarPorGenero(@PathVariable String genero) {
        return productoService.buscarPorGenero(genero);
    }

    @GetMapping("/plataforma/{plataforma}")
    public List<Producto> buscarPorPlataforma(@PathVariable String plataforma) {
        return productoService.buscarPorPlataforma(plataforma);
    }

    @GetMapping("/estado/{estado}")
    public List<Producto> buscarPorEstado(@PathVariable String estado) {
        return productoService.buscarPorEstado(estado);
    }

    @GetMapping("/precio")
    public List<Producto> buscarPorPrecio(@RequestParam double min, @RequestParam double max) {
        return productoService.buscarPorPrecio(min, max);
    }

    @GetMapping("/rating/{min}")
    public List<Producto> buscarPorRating(@PathVariable double min) {
        return productoService.buscarPorRating(min);
    }
}
