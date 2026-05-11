package com.reservas.juegos.rawg;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class RawgJuegoDTO {

    private Long id;

    @JsonProperty("name")
    private String nombre;

    @JsonProperty("background_image")
    private String imagenUrl;

    private List<GeneroDTO> genres;

    // ── Clase interna: solo nos interesa el nombre del género ──
    public static class GeneroDTO {
        private String name;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }

    // ── Getters y Setters ──

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }

    public List<GeneroDTO> getGenres() { return genres; }
    public void setGenres(List<GeneroDTO> genres) { this.genres = genres; }
}
