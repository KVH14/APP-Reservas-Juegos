package com.reservas.juegos.dto;

public class ReservaDTO {
    private Long usuarioId;
    private Long productoId;
    private String fecha;

    public ReservaDTO() {}

    public ReservaDTO(Long usuarioId, Long productoId, String fecha) {
        this.usuarioId = usuarioId;
        this.productoId = productoId;
        this.fecha = fecha;
    }

    public Long getUsuarioId() { return usuarioId; }
    public Long getProductoId() { return productoId; }
    public String getFecha() { return fecha; }
}
