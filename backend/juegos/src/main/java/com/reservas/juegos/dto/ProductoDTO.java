package com.reservas.juegos.dto;

public class ProductoDTO {

    private Long id;
    private String titulo;
    private String plataforma;
    private String genero;
    private double precio;
    private int stock;
    private String estado;
    private double rating;
    private String emoji;
    private String politicas;
    private String imagenUrl;
    private Long categoriaId;
    private Long rawgId;          // ← agregado para el endpoint importarRawg

    // Constructor vacío
    public ProductoDTO() {}

    // Constructor con parámetros
    public ProductoDTO(Long id, String titulo, String plataforma, String genero,
                       double precio, int stock, String estado, double rating,
                       String emoji, String politicas, String imagenUrl, Long categoriaId, Long rawgId) {
        this.id = id;
        this.titulo = titulo;
        this.plataforma = plataforma;
        this.genero = genero;
        this.precio = precio;
        this.stock = stock;
        this.estado = estado;
        this.rating = rating;
        this.emoji = emoji;
        this.politicas = politicas;
        this.imagenUrl = imagenUrl;
        this.categoriaId = categoriaId;
        this.rawgId = rawgId;
    }

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getPlataforma() { return plataforma; }
    public void setPlataforma(String plataforma) { this.plataforma = plataforma; }

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    public String getEmoji() { return emoji; }
    public void setEmoji(String emoji) { this.emoji = emoji; }

    public String getPoliticas() { return politicas; }
    public void setPoliticas(String politicas) { this.politicas = politicas; }

    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }

    public Long getCategoriaId() { return categoriaId; }
    public void setCategoriaId(Long categoriaId) { this.categoriaId = categoriaId; }

    public Long getRawgId() { return rawgId; }
    public void setRawgId(Long rawgId) { this.rawgId = rawgId; }
}