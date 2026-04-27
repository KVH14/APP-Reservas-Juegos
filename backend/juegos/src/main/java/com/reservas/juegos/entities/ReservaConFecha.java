package com.reservas.juegos.entities;

import java.time.LocalDate;

public class ReservaConFecha extends Reserva {
    public ReservaConFecha(String nombreCliente, String emailCliente, Producto producto, LocalDate fechaReserva, LocalDate fechaDevolucion) {
        super(nombreCliente, emailCliente, producto, fechaReserva, fechaDevolucion);
    }
}
