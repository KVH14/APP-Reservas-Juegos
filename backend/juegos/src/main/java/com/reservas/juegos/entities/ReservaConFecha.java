package com.reservas.juegos.entities;

public class ReservaConFecha extends Reserva {
    @Override
    public String getDetalles() {
        return "Reserva con fecha: " + getFecha();
    }
}
