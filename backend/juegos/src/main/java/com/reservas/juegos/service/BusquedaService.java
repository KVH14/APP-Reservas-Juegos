package com.reservas.juegos.service;

import com.reservas.juegos.entities.Producto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BusquedaService {
    private final List<Producto> productos;

    public BusquedaService(List<Producto> productos) {
        this.productos = productos;
    }

    public List<Producto> buscarPorNombre(String nombre) {
        return productos.stream()
                .filter(p -> p.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .collect(Collectors.toList());
    }
}
