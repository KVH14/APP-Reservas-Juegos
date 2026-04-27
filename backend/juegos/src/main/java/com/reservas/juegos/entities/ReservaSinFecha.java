package com.reservas.juegos.entities;

public class ReservaSinFecha extends Reserva {
    @Override
    public String getDetalles() {
        return "Reserva sin fecha definida";
    }
}
