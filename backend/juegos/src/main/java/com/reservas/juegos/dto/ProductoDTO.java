package com.reservas.juegos.dto;

public class ProductoDTO {
    private String nombre;
    private String descripcion;
    private Double precio;
    private String categoriaNombre;

    public ProductoDTO() {}

    public ProductoDTO(String nombre, String descripcion, Double precio, String categoriaNombre) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.categoriaNombre = categoriaNombre;
    }

    // Getters y setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }
    public String getCategoriaNombre() { return categoriaNombre; }
    public void setCategoriaNombre(String categoriaNombre) { this.categoriaNombre = categoriaNombre; }
}
