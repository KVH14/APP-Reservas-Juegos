package com.reservas.juegos.repository;

import com.reservas.juegos.entities.Caracteristica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CaracteristicaRepository extends JpaRepository<Caracteristica, Long> {
    List<Caracteristica> findByProductoId(Long productoId);
    void deleteByProductoId(Long productoId);
}
