package com.reservas.juegos.entities;

public class Reserva {
    private Long id;
    private String fecha;
    private String detalles;
    private Usuario usuario;   // referencia al usuario
    private Producto producto; // referencia al producto

    public Reserva(Long id, String fecha, String detalles, Usuario usuario, Producto producto) {
        this.id = id;
        this.fecha = fecha;
        this.detalles = detalles;
        this.usuario = usuario;
        this.producto = producto;
    }

    public Long getId() { return id; }
    public String getFecha() { return fecha; }
    public String getDetalles() { return detalles; }
    public Usuario getUsuario() { return usuario; }
    public Producto getProducto() { return producto; }
}
