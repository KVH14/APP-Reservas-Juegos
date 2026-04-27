package com.reservas.juegos.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "reservas")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Datos del cliente
    @Column(nullable = false)
    private String nombreCliente;

    @Column(nullable = false)
    private String emailCliente;

    // Producto reservado
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "caracteristicas", "categorias"})
    private Producto producto;

    @Column(nullable = false)
    private LocalDate fechaReserva;

    private LocalDate fechaDevolucion;

    // Estado: PENDIENTE, CONFIRMADA, CANCELADA
    @Column(nullable = false)
    private String estado;

    public Reserva() {}

    public Reserva(String nombreCliente, String emailCliente, Producto producto,
                   LocalDate fechaReserva, LocalDate fechaDevolucion) {
        this.nombreCliente = nombreCliente;
        this.emailCliente = emailCliente;
        this.producto = producto;
        this.fechaReserva = fechaReserva;
        this.fechaDevolucion = fechaDevolucion;
        this.estado = "PENDIENTE";
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }

    public String getEmailCliente() { return emailCliente; }
    public void setEmailCliente(String emailCliente) { this.emailCliente = emailCliente; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }

    public LocalDate getFechaReserva() { return fechaReserva; }
    public void setFechaReserva(LocalDate fechaReserva) { this.fechaReserva = fechaReserva; }

    public LocalDate getFechaDevolucion() { return fechaDevolucion; }
    public void setFechaDevolucion(LocalDate fechaDevolucion) { this.fechaDevolucion = fechaDevolucion; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
