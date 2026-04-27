package com.reservas.juegos.controller;

import com.reservas.juegos.entities.Producto;
import com.reservas.juegos.service.DisponibilidadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/disponibilidad")
public class DisponibilidadController {

    @Autowired
    private DisponibilidadService disponibilidadService;

    @PostMapping("/agregar")
    public void agregarProducto(@RequestBody Producto producto) {
        disponibilidadService.agregarProducto(producto);
    }

    @GetMapping("/{id}")
    public String verificar(@PathVariable Long id) {
        boolean disponible = disponibilidadService.verificarDisponibilidad(id);
        return disponible ? "Disponible" : "No disponible";
    }

    @GetMapping
    public List<Producto> listar() {
        return disponibilidadService.listarProductos();
    }
}
