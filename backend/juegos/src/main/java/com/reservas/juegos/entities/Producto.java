package com.reservas.juegos.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "productos")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    private String plataforma;
    private String genero;
    private double precio;
    private int stock;
    private String estado;
    private double rating;
    private String emoji;

    // Identificador externo RAWG
    private Long rawgId;

    @Column(columnDefinition = "TEXT")
    private String politicas;

    private String imagenUrl;

    private int totalVotos;
    private double sumaRatings;

    @JsonIgnore
    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Caracteristica> caracteristicas = new ArrayList<>();

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "producto_categoria",
        joinColumns = @JoinColumn(name = "producto_id"),
        inverseJoinColumns = @JoinColumn(name = "categoria_id")
    )
    private List<Categoria> categorias = new ArrayList<>();

    public Producto() {}

    public Producto(String titulo, String plataforma, String genero,
                    double precio, int stock, String estado, double rating, String emoji) {
        this.titulo = titulo;
        this.plataforma = plataforma;
        this.genero = genero;
        this.precio = precio;
        this.stock = stock;
        this.estado = estado;
        this.rating = rating;
        this.emoji = emoji;
    }

    // --- Getters y Setters ---
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

    public Long getRawgId() { return rawgId; }
    public void setRawgId(Long rawgId) { this.rawgId = rawgId; }

    public List<Caracteristica> getCaracteristicas() { return caracteristicas; }
    public void setCaracteristicas(List<Caracteristica> caracteristicas) { this.caracteristicas = caracteristicas; }

    public List<Categoria> getCategorias() { return categorias; }
    public void setCategorias(List<Categoria> categorias) { this.categorias = categorias; }

    public String getPoliticas() { return politicas; }
    public void setPoliticas(String politicas) { this.politicas = politicas; }

    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }

    public int getTotalVotos() { return totalVotos; }
    public void setTotalVotos(int totalVotos) { this.totalVotos = totalVotos; }

    public double getSumaRatings() { return sumaRatings; }
    public void setSumaRatings(double sumaRatings) { this.sumaRatings = sumaRatings; }
}
