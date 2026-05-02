package com.reservas.juegos.repository;

import com.reservas.juegos.entities.Favorito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoritoRepository extends JpaRepository<Favorito, Long> {
    boolean existsByUsuarioIdAndProductoId(Long usuarioId, Long productoId);
    List<Favorito> findByUsuarioId(Long usuarioId);
}
