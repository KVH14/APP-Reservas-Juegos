package com.reservas.juegos.entities;

public class Favorito {
    private Long id;
    private Usuario usuario;
    private Producto producto;

    public Favorito(Long id, Usuario usuario, Producto producto) {
        this.id = id;
        this.usuario = usuario;
        this.producto = producto;
    }

    public Long getId() { return id; }
    public Usuario getUsuario() { return usuario; }
    public Producto getProducto() { return producto; }
}
