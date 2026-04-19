package com.reservas.juegos.entities;

// Clase que representa una tarea dentro del sistema
public class Tarea {

    private String nombre;
    private int prioridad;

    // Constructor vacío (necesario para Spring al recibir JSON)
    public Tarea() {}

    // Constructor con parámetros
    public Tarea(String nombre, int prioridad) {
        this.nombre = nombre;
        this.prioridad = prioridad;
    }

    // Getters y setters (acceso a los atributos)
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getPrioridad() { return prioridad; }
    public void setPrioridad(int prioridad) { this.prioridad = prioridad; }
}