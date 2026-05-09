package com.reservas.juegos.repository;

import com.reservas.juegos.entities.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByGenero(String genero);
    List<Producto> findByPlataforma(String plataforma);
    List<Producto> findByEstado(String estado);
    List<Producto> findByPrecioBetween(double min, double max);
    List<Producto> findByRatingGreaterThanEqual(double rating);
}
