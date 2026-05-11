package com.reservas.juegos.service;

import com.reservas.juegos.entities.Categoria;
import com.reservas.juegos.entities.Producto;
import com.reservas.juegos.repository.CategoriaRepository;
import com.reservas.juegos.repository.ProductoRepository;
import com.reservas.juegos.rawg.RawgJuegoDTO;
import com.reservas.juegos.rawg.RawgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    // Servicio RAWG
    @Autowired
    private RawgService rawgService;

    @Value("${rawg.api.key}")
    private String rawgApiKey;

    @Value("${rawg.api.url}")
    private String rawgApiUrl;

    // ── Listar todos los productos ──
    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    public Page<Producto> listarPaginado(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productoRepository.findAll(pageable);
    }

    public Optional<Producto> buscarPorId(Long id) {
        return productoRepository.findById(id);
    }

    // ── Crear producto ──
    public Producto crear(Producto producto) {
        if (producto.getTitulo() == null || producto.getTitulo().isBlank()) {
            throw new IllegalArgumentException("El título del producto es obligatorio");
        }
        return productoRepository.save(producto);
    }

    // ── Actualizar producto ──
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
            p.setRawgId(datos.getRawgId());
            return productoRepository.save(p);
        });
    }

    // ── Eliminar producto ──
    public boolean eliminar(Long id) {
        if (!productoRepository.existsById(id)) {
            return false;
        }
        productoRepository.deleteById(id);
        return true;
    }

    // ── Categorías ──
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

    // ── Políticas y compartir ──
    public Optional<String> obtenerPoliticas(Long id) {
        return productoRepository.findById(id)
                .map(Producto::getPoliticas);
    }

    public Optional<Map<String, String>> obtenerDatosCompartir(Long id) {
        return productoRepository.findById(id).map(p -> Map.of(
                "titulo",     p.getTitulo(),
                "precio",     String.valueOf(p.getPrecio()),
                "plataforma", p.getPlataforma() != null ? p.getPlataforma() : "",
                "imagenUrl",  p.getImagenUrl() != null ? p.getImagenUrl() : "",
                "link",       "https://juegos.app/producto/" + p.getId()
        ));
    }

    // ── Rating ──
    public Optional<Producto> puntuar(Long id, double puntuacion) {
        if (puntuacion < 1 || puntuacion > 5) return Optional.empty();

        return productoRepository.findById(id).map(p -> {
            p.setTotalVotos(p.getTotalVotos() + 1);
            p.setSumaRatings(p.getSumaRatings() + puntuacion);
            double promedio = p.getSumaRatings() / p.getTotalVotos();
            p.setRating(Math.round(promedio * 10.0) / 10.0);
            return productoRepository.save(p);
        });
    }

    // ── Importar juego desde RAWG ──
    public Producto importarDesdeRawg(Long rawgId, double precio, int stock, String plataforma) {
        RawgJuegoDTO rawgJuego = rawgService.obtenerPorId(rawgId);

        if (rawgJuego == null) {
            throw new RuntimeException("No se encontró el juego en RAWG con id " + rawgId);
        }

        Producto producto = new Producto();
        producto.setTitulo(rawgJuego.getNombre());       // título real desde RAWG
        producto.setImagenUrl(rawgJuego.getImagenUrl()); // imagen real desde RAWG
        producto.setRawgId(rawgId);
        producto.setPrecio(precio);
        producto.setStock(stock);
        producto.setPlataforma(plataforma);
        producto.setEstado("DISPONIBLE");

        // opcional: guardar género si existe
        if (rawgJuego.getGenres() != null && !rawgJuego.getGenres().isEmpty()) {
            producto.setGenero(rawgJuego.getGenres().get(0).getName());
        }

        return productoRepository.save(producto);
    }
}
