package com.reservas.juegos.repository;

import com.reservas.juegos.entities.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    List<Reserva> findByEmailCliente(String emailCliente);
    List<Reserva> findByProductoId(Long productoId);
    List<Reserva> findByEstado(String estado);
}
