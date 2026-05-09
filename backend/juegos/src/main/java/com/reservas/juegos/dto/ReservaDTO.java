package com.reservas.juegos.dto;

import java.time.LocalDate;

public class ReservaDTO {
    private Long id;
    private String nombreCliente;
    private String emailCliente;
    private Long productoId;        // solo pasamos el ID del producto
    private LocalDate fechaReserva;
    private LocalDate fechaDevolucion;
    private String tipo;            // NORMAL o PREMIUM
    private String estado;          // PENDIENTE, CONFIRMADA, CANCELADA

    public ReservaDTO() {}

    public ReservaDTO(Long id, String nombreCliente, String emailCliente,
                      Long productoId, LocalDate fechaReserva, LocalDate fechaDevolucion,
                      String tipo, String estado) {
        this.id = id;
        this.nombreCliente = nombreCliente;
        this.emailCliente = emailCliente;
        this.productoId = productoId;
        this.fechaReserva = fechaReserva;
        this.fechaDevolucion = fechaDevolucion;
        this.tipo = tipo;
        this.estado = estado;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }
    public String getEmailCliente() { return emailCliente; }
    public void setEmailCliente(String emailCliente) { this.emailCliente = emailCliente; }
    public Long getProductoId() { return productoId; }
    public void setProductoId(Long productoId) { this.productoId = productoId; }
    public LocalDate getFechaReserva() { return fechaReserva; }
    public void setFechaReserva(LocalDate fechaReserva) { this.fechaReserva = fechaReserva; }
    public LocalDate getFechaDevolucion() { return fechaDevolucion; }
    public void setFechaDevolucion(LocalDate fechaDevolucion) { this.fechaDevolucion = fechaDevolucion; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
