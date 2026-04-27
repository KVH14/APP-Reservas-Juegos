package com.reservas.juegos.entities;

public class Producto {
    private Long id;
    private String nombre;
    private String descripcion;
    private Double precio;
    private String categoriaNombre;

    public Producto(Long id, String nombre, String descripcion, Double precio, String categoriaNombre) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.categoriaNombre = categoriaNombre;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public Double getPrecio() { return precio; }
    public String getCategoriaNombre() { return categoriaNombre; }

    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setPrecio(Double precio) { this.precio = precio; }
    public void setCategoriaNombre(String categoriaNombre) { this.categoriaNombre = categoriaNombre; }
}
