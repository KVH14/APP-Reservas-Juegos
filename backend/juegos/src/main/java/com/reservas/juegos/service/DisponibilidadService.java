package com.reservas.juegos.service;

import com.reservas.juegos.entities.Producto;
import com.reservas.juegos.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DisponibilidadService {

    private final ProductoRepository productoRepository;

    public DisponibilidadService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }
    /** Verificar disponibilidad de un producto por ID */
    public boolean verificarDisponibilidad(Long productoId) {
        Optional<Producto> productoOpt = productoRepository.findById(productoId);
        if (productoOpt.isEmpty()) return false;

        Producto producto = productoOpt.get();
        return producto.getStock() > 0 &&
                producto.getEstado() != null &&
                producto.getEstado().equalsIgnoreCase("DISPONIBLE");
    }

    /** Cambiar disponibilidad de un producto */
    public boolean cambiarDisponibilidad(Long productoId, boolean disponible) {
        Optional<Producto> productoOpt = productoRepository.findById(productoId);
        if (productoOpt.isEmpty()) return false;

        Producto producto = productoOpt.get();
        producto.setEstado(disponible ? "DISPONIBLE" : "NO_DISPONIBLE");
        productoRepository.save(producto);
        return true;
    }
}
