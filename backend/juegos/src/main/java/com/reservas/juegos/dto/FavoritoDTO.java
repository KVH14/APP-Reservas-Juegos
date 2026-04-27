package com.reservas.juegos.dto;

public class FavoritoDTO {
    private Long usuarioId;
    private Long productoId;

    public FavoritoDTO() {}

    public FavoritoDTO(Long usuarioId, Long productoId) {
        this.usuarioId = usuarioId;
        this.productoId = productoId;
    }

    // Getters y setters
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    public Long getProductoId() { return productoId; }
    public void setProductoId(Long productoId) { this.productoId = productoId; }
}
