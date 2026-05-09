package com.reservas.juegos.service;

import com.reservas.juegos.entities.Producto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DisponibilidadService {
    private final List<Producto> productos = new ArrayList<>();

    public void agregarProducto(Producto producto) {
        productos.add(producto);
    }

    public boolean verificarDisponibilidad(Long id) {
        return productos.stream()
                .filter(p -> p.getId() != null && p.getId().equals(id))
                .map(p -> p.getEstado() != null && p.getEstado().equalsIgnoreCase("disponible"))
                .findFirst()
                .orElse(false);
    }

    public boolean cambiarDisponibilidad(Long id, boolean disponible) {
        return productos.stream()
                .filter(p -> p.getId() != null && p.getId().equals(id))
                .findFirst()
                .map(p -> {
                    p.setEstado(disponible ? "disponible" : "no_disponible");
                    return true;
                })
                .orElse(false);
    }
}