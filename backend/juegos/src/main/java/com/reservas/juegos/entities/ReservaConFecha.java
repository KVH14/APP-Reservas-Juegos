package com.reservas.juegos.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.time.LocalDate;

@Entity
@DiscriminatorValue("CON_FECHA")
public class ReservaConFecha extends Reserva {
    public ReservaConFecha(String nombreCliente, String emailCliente, Producto producto,
                           LocalDate fechaReserva, LocalDate fechaDevolucion) {
        super(nombreCliente, emailCliente, producto, fechaReserva, fechaDevolucion);
    }

    public ReservaConFecha() {
        super();
    }
}