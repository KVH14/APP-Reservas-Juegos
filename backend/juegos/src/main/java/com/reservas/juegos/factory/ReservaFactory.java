package com.reservas.juegos.factory;

import com.reservas.juegos.entities.Reserva;
import com.reservas.juegos.entities.Usuario;
import com.reservas.juegos.entities.Producto;

public class ReservaFactory {
    public static Reserva crearReserva(Long id, String fecha, Usuario usuario, Producto producto) {
        if (fecha != null && !fecha.isEmpty()) {
            return new Reserva(id, fecha, "Reserva con fecha: " + fecha, usuario, producto);
        } else {
            return new Reserva(id, "No definida", "Reserva sin fecha definida", usuario, producto);
        }
    }
}
