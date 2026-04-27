package com.reservas.juegos.service;

import com.reservas.juegos.dto.ProductoDTO;
import com.reservas.juegos.entities.Producto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ProductoService {

    private final List<Producto> productos = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public List<Producto> listarTodos() {
        return productos;
    }

    public Optional<Producto> buscarPorId(Long id) {
        return productos.stream().filter(p -> p.getId().equals(id)).findFirst();
    }

    public Producto crear(ProductoDTO dto) {
        Producto producto = new Producto(
                idGenerator.getAndIncrement(),
                dto.getNombre(),
                dto.getDescripcion(),
                dto.getPrecio(),
                dto.getCategoriaNombre()
        );
        productos.add(producto);
        return producto;
    }

    public Optional<Producto> actualizar(Long id, ProductoDTO dto) {
        return buscarPorId(id).map(prod -> {
            prod.setNombre(dto.getNombre());
            prod.setDescripcion(dto.getDescripcion());
            prod.setPrecio(dto.getPrecio());
            prod.setCategoriaNombre(dto.getCategoriaNombre());
            return prod;
        });
    }

    public boolean eliminar(Long id) {
        return productos.removeIf(p -> p.getId().equals(id));
    }
}
