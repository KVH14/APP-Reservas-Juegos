package com.reservas.juegos.factory;

import com.reservas.juegos.entities.Producto;
import com.reservas.juegos.entities.Reserva;
import com.reservas.juegos.entities.ReservaConFecha;
import com.reservas.juegos.entities.ReservaSinFecha;

import java.time.LocalDate;

public class ReservaFactory {

    /** Crear reserva con fecha */
    public static Reserva crearReservaConFecha(
            String nombreCliente,
            String emailCliente,
            Producto producto,
            LocalDate fechaReserva,
            LocalDate fechaDevolucion
    ) {
        return new ReservaConFecha(nombreCliente, emailCliente, producto, fechaReserva, fechaDevolucion);
    }

    /** Crear reserva sin fecha (usa fecha actual y sin devolución) */
    public static Reserva crearReservaSinFecha(
            String nombreCliente,
            String emailCliente,
            Producto producto
    ) {
        return new ReservaSinFecha(nombreCliente, emailCliente, producto);
    }
}
