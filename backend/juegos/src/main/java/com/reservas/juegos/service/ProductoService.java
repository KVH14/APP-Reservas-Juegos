package com.reservas.juegos.service;

import com.reservas.juegos.dto.ProductoDTO;
import com.reservas.juegos.entities.Categoria;
import com.reservas.juegos.entities.Producto;
import com.reservas.juegos.repository.CategoriaRepository;
import com.reservas.juegos.repository.ProductoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProductoService(ProductoRepository productoRepository, CategoriaRepository categoriaRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    // 🔹 CRUD básico
    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    public Page<Producto> listarPaginado(int page, int size) {
        return productoRepository.findAll(PageRequest.of(page, size));
    }

    public Optional<Producto> buscarPorId(Long id) {
        return productoRepository.findById(id);
    }

    public Producto crear(Producto producto) {
        return productoRepository.save(producto);
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

    // 🔹 Métodos de búsqueda (para BusquedaController)
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

    // 🔹 Asignar / quitar categorías
    public Optional<Producto> asignarCategoria(Long productoId, Long categoriaId) {
        Optional<Producto> productoOpt = productoRepository.findById(productoId);
        Optional<Categoria> categoriaOpt = categoriaRepository.findById(categoriaId);

        if (productoOpt.isPresent() && categoriaOpt.isPresent()) {
            Producto producto = productoOpt.get();
            producto.getCategorias().add(categoriaOpt.get());
            return Optional.of(productoRepository.save(producto));
        }
        return Optional.empty();
    }

    public Optional<Producto> quitarCategoria(Long productoId, Long categoriaId) {
        Optional<Producto> productoOpt = productoRepository.findById(productoId);
        Optional<Categoria> categoriaOpt = categoriaRepository.findById(categoriaId);

        if (productoOpt.isPresent() && categoriaOpt.isPresent()) {
            Producto producto = productoOpt.get();
            producto.getCategorias().remove(categoriaOpt.get());
            return Optional.of(productoRepository.save(producto));
        }
        return Optional.empty();
    }

    // 🔹 Políticas
    public Optional<String> obtenerPoliticas(Long id) {
        return productoRepository.findById(id).map(Producto::getPoliticas);
    }

    // 🔹 Compartir (ejemplo: devolver título e imagen)
    public Optional<Map<String, String>> obtenerDatosCompartir(Long id) {
        return productoRepository.findById(id).map(p -> {
            Map<String, String> datos = new HashMap<>();
            datos.put("titulo", p.getTitulo());
            datos.put("imagenUrl", p.getImagenUrl());
            return datos;
        });
    }

    // 🔹 Puntuar producto
    public Optional<Producto> puntuar(Long id, double puntuacion) {
        return productoRepository.findById(id).map(p -> {
            p.setTotalVotos(p.getTotalVotos() + 1);
            p.setSumaRatings(p.getSumaRatings() + puntuacion);
            p.setRating(p.getSumaRatings() / p.getTotalVotos());
            return productoRepository.save(p);
        });
    }
}
