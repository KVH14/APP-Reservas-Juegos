package com.reservas.juegos.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.time.LocalDate;

@Entity
@DiscriminatorValue("SIN_FECHA")
public class ReservaSinFecha extends Reserva {
    public ReservaSinFecha(String nombreCliente, String emailCliente, Producto producto) {
        super(nombreCliente, emailCliente, producto, LocalDate.now(), null);
    }

    public ReservaSinFecha() {
        super();
    }
}
