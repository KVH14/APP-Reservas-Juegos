package com.reservas.juegos.entities;

import java.time.LocalDate;

public class ReservaSinFecha extends Reserva {
    public ReservaSinFecha(String nombreCliente, String emailCliente, Producto producto) {
        super(nombreCliente, emailCliente, producto, LocalDate.now(), null);
    }
}
