package com.reservas.juegos.service;

import com.reservas.juegos.entities.Producto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DisponibilidadService {

    private final List<Producto> productos = new ArrayList<>();

    // Simulación: agregar productos en memoria
    public void agregarProducto(Producto producto) {
        productos.add(producto);
    }

    public boolean verificarDisponibilidad(Long id) {
        return productos.stream()
                .filter(p -> p.getId().equals(id))
                .map(Producto::isDisponible)
                .findFirst()
                .orElse(false);
    }

    public List<Producto> listarProductos() {
        return productos;
    }
}
