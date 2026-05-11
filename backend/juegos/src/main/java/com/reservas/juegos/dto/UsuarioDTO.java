package com.reservas.juegos.dto;

public class UsuarioDTO {
    private String nombre;
    private String correo;

    public UsuarioDTO() {}

    public UsuarioDTO(String nombre, String correo) {
        this.nombre = nombre;
        this.correo = correo;
    }

    public String getNombre() { return nombre; }
    public String getCorreo() { return correo; }
}
