package com.reservas.juegos.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "categorias")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    private String emoji;

    private int cantidadJuegos;

    public Categoria() {}

    public Categoria(String nombre, String emoji, int cantidadJuegos) {
        this.nombre = nombre;
        this.emoji = emoji;
        this.cantidadJuegos = cantidadJuegos;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmoji() { return emoji; }
    public void setEmoji(String emoji) { this.emoji = emoji; }

    public int getCantidadJuegos() { return cantidadJuegos; }
    public void setCantidadJuegos(int cantidadJuegos) { this.cantidadJuegos = cantidadJuegos; }
}
