package com.reservas.juegos.factory;

import com.reservas.juegos.entities.Producto;
import com.reservas.juegos.entities.Reserva;

import java.time.LocalDate;

public class ReservaFactory {

    // Crear reserva con fecha (usa tipos del entity Reserva)
    public static Reserva crearReservaConFecha(String nombreCliente, String emailCliente, Producto producto, LocalDate fechaReserva, LocalDate fechaDevolucion) {
        return new Reserva(nombreCliente, emailCliente, producto, fechaReserva, fechaDevolucion);
    }

    // Crear reserva sin fecha: pone fechaReserva = hoy y fechaDevolucion = null
    public static Reserva crearReservaSinFecha(String nombreCliente, String emailCliente, Producto producto) {
        return new Reserva(nombreCliente, emailCliente, producto, LocalDate.now(), null);
    }
}
