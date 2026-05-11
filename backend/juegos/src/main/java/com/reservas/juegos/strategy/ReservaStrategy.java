package com.reservas.juegos.strategy;

import com.reservas.juegos.dto.ReservaDTO;
import com.reservas.juegos.entities.Producto;
import com.reservas.juegos.entities.Reserva;

public interface ReservaStrategy {
    Reserva crear(ReservaDTO dto, Producto producto);
}