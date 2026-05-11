package com.reservas.juegos.service;

import com.reservas.juegos.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DisponibilidadService {

    @Autowired
    private ProductoRepository productoRepository;

    public boolean verificarDisponibilidad(Long id) {
        return productoRepository.findById(id)
                .map(p -> p.getStock() > 0 && !"agotado".equalsIgnoreCase(p.getEstado()))
                .orElse(false);
    }

    public boolean cambiarDisponibilidad(Long id, boolean disponible) {
        return productoRepository.findById(id)
                .map(p -> {
                    p.setEstado(disponible ? "disponible" : "no_disponible");
                    productoRepository.save(p);
                    return true;
                })
                .orElse(false);
    }
}