package com.reservas.juegos.service;

import com.reservas.juegos.dto.ProductoDTO;
import com.reservas.juegos.entities.Categoria;
import com.reservas.juegos.entities.Producto;
import com.reservas.juegos.repository.CategoriaRepository;
import com.reservas.juegos.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProductoService(ProductoRepository productoRepository, CategoriaRepository categoriaRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    // CRUD básico
    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    public Optional<Producto> buscarPorId(Long id) {
        return productoRepository.findById(id);
    }

    public Producto crearProducto(ProductoDTO dto) {
        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new IllegalArgumentException("La categoría no existe"));

        Producto nuevo = new Producto(dto.getTitulo(), dto.getPlataforma(), dto.getGenero(),
                dto.getPrecio(), dto.getStock(), dto.getEstado(), dto.getRating(), dto.getEmoji());
        nuevo.setPoliticas(dto.getPoliticas());
        nuevo.setImagenUrl(dto.getImagenUrl());
        nuevo.getCategorias().add(categoria);

        return productoRepository.save(nuevo);
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
            p.setPoliticas(datos.getPoliticas());
            p.setImagenUrl(datos.getImagenUrl());
            return productoRepository.save(p);
        });
    }

    public boolean eliminar(Long id) {
        if (productoRepository.existsById(id)) {
            productoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Métodos de búsqueda para BusquedaController
    public List<Producto> buscarPorGenero(String genero) {
        return productoRepository.findByGenero(genero);
    }

    public List<Producto> buscarPorPlataforma(String plataforma) {
        return productoRepository.findByPlataforma(plataforma);
    }

    public List<Producto> buscarPorEstado(String estado) {
        return productoRepository.findByEstado(estado);
    }

    public List<Producto> buscarPorPrecio(double min, double max) {
        return productoRepository.findByPrecioBetween(min, max);
    }

    public List<Producto> buscarPorRating(double min) {
        return productoRepository.findByRatingGreaterThanEqual(min);
    }
}
