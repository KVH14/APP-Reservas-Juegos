package com.reservas.juegos.service;

import com.reservas.juegos.entities.Favorito;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class FavoritoService {

    private final List<Favorito> favoritos = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public Favorito marcarFavorito(Long usuarioId, Long productoId) {
        // Validar duplicados
        boolean existe = favoritos.stream()
                .anyMatch(f -> f.getUsuarioId().equals(usuarioId) && f.getProductoId().equals(productoId));
        if (existe) {
            throw new IllegalArgumentException("Ya está marcado como favorito");
        }

        Favorito favorito = new Favorito(idGenerator.getAndIncrement(), usuarioId, productoId);
        favoritos.add(favorito);
        return favorito;
    }

    public List<Favorito> listarFavoritos(Long usuarioId) {
        return favoritos.stream()
                .filter(f -> f.getUsuarioId().equals(usuarioId))
                .collect(Collectors.toList());
    }

    public boolean eliminarFavorito(Long usuarioId, Long productoId) {
        return favoritos.removeIf(f -> f.getUsuarioId().equals(usuarioId) && f.getProductoId().equals(productoId));
    }
}
