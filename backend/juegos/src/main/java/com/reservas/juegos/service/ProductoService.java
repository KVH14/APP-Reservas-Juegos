package com.reservas.juegos.service;

import com.reservas.juegos.entities.Categoria;
import com.reservas.juegos.entities.Producto;
import com.reservas.juegos.repository.CategoriaRepository;
import com.reservas.juegos.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    // Listar productos con paginación
    public Page<Producto> listarPaginado(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productoRepository.findAll(pageable);
    }

    public Optional<Producto> buscarPorId(Long id) {
        return productoRepository.findById(id);
    }

    public Producto crear(Producto producto) {
        return productoRepository.save(producto);
    }

    public Optional<Producto> actualizar(Long id, Producto datos) {
        return productoRepository.findById(id).map(p -> {
            p.setTitulo(datos.getTitulo());
            p.setPlataforma(datos.getPlataforma());
            p.setGenero(datos.getGenero());
            p.setPrecio(datos.getPrecio());
            p.setStock(datos.getStock());
            p.setEstado(datos.getEstado());
            p.setRating(datos.getRating());
            p.setEmoji(datos.getEmoji());
            return productoRepository.save(p);
        });
    }

    public boolean eliminar(Long id) {
        if (!productoRepository.existsById(id)) {
            return false;
        }
        productoRepository.deleteById(id);
        return true;
    }

    // #12 Categorizar producto: asigna una categoría a un producto
    public Optional<Producto> asignarCategoria(Long productoId, Long categoriaId) {
        Optional<Producto> productoOpt = productoRepository.findById(productoId);
        Optional<Categoria> categoriaOpt = categoriaRepository.findById(categoriaId);

        if (productoOpt.isEmpty() || categoriaOpt.isEmpty()) {
            return Optional.empty();
        }

        Producto producto = productoOpt.get();
        Categoria categoria = categoriaOpt.get();

        if (!producto.getCategorias().contains(categoria)) {
            producto.getCategorias().add(categoria);
            productoRepository.save(producto);
        }
        return Optional.of(producto);
    }

    public Optional<Producto> quitarCategoria(Long productoId, Long categoriaId) {
        return productoRepository.findById(productoId).map(p -> {
            p.getCategorias().removeIf(c -> c.getId().equals(categoriaId));
            return productoRepository.save(p);
        });
    }
}
