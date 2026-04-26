package com.reservas.juegos.dto;

public class CaracteristicaDTO {

    private String clave;
    private String valor;
    private Long productoId;

    public CaracteristicaDTO() {}

    public CaracteristicaDTO(String clave, String valor, Long productoId) {
        this.clave = clave;
        this.valor = valor;
        this.productoId = productoId;
    }

    public String getClave() { return clave; }
    public void setClave(String clave) { this.clave = clave; }

    public String getValor() { return valor; }
    public void setValor(String valor) { this.valor = valor; }

    public Long getProductoId() { return productoId; }
    public void setProductoId(Long productoId) { this.productoId = productoId; }
}
