package com.reservas.juegos.strategy;

import org.springframework.stereotype.Component;
import com.reservas.juegos.dto.ReservaDTO;
import com.reservas.juegos.entities.Producto;
import com.reservas.juegos.entities.Reserva;
import com.reservas.juegos.factory.ReservaFactory;

@Component
public class ReservaPremiumStrategy implements ReservaStrategy {

    @Override
    public Reserva crear(ReservaDTO dto, Producto producto) {

        Reserva r = ReservaFactory.crearReservaConFecha(
                dto.getNombreCliente(),
                dto.getEmailCliente(),
                producto,
                dto.getFechaReserva(),
                dto.getFechaDevolucion()
        );

        r.setEstado("PREMIUM");
        return r;
    }
}