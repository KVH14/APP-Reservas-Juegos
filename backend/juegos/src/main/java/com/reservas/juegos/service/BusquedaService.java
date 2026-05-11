package com.reservas.juegos.service;

import com.reservas.juegos.entities.Producto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BusquedaService {
    private final ProductoService productoService;

    public BusquedaService(ProductoService productoService) {
        this.productoService = productoService;
    }

    public List<Producto> buscarPorNombre(String nombre) {
        return productoService.listarTodos().stream()
                .filter(p -> p.getTitulo() != null && p.getTitulo().toLowerCase().contains(nombre.toLowerCase()))
                .collect(Collectors.toList());
    }
}
