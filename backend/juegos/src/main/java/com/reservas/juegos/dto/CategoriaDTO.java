package com.reservas.juegos.dto;

public class CategoriaDTO {

    private String nombre;
    private String emoji;
    private int cantidadJuegos;

    public CategoriaDTO() {}

    public CategoriaDTO(String nombre, String emoji, int cantidadJuegos) {
        this.nombre = nombre;
        this.emoji = emoji;
        this.cantidadJuegos = cantidadJuegos;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmoji() { return emoji; }
    public void setEmoji(String emoji) { this.emoji = emoji; }

    public int getCantidadJuegos() { return cantidadJuegos; }
    public void setCantidadJuegos(int cantidadJuegos) { this.cantidadJuegos = cantidadJuegos; }
}
