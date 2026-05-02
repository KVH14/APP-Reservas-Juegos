package com.reservas.juegos.service;

import com.reservas.juegos.dto.FavoritoDTO;
import com.reservas.juegos.entities.Favorito;
import com.reservas.juegos.entities.Producto;
import com.reservas.juegos.entities.Usuario;
import com.reservas.juegos.repository.FavoritoRepository;
import com.reservas.juegos.repository.ProductoRepository;
import com.reservas.juegos.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoritoService {
    private final FavoritoRepository favoritoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;

    public FavoritoService(
            FavoritoRepository favoritoRepository,
            UsuarioRepository usuarioRepository,
            ProductoRepository productoRepository) {
        this.favoritoRepository = favoritoRepository;
        this.usuarioRepository = usuarioRepository;
        this.productoRepository = productoRepository;
    }

    public Favorito crearFavorito(FavoritoDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        Producto producto = productoRepository.findById(dto.getProductoId())
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

        if (favoritoRepository.existsByUsuarioIdAndProductoId(dto.getUsuarioId(), dto.getProductoId())) {
            throw new IllegalArgumentException("El favorito ya existe para este usuario y producto");
        }

        Favorito favorito = new Favorito(usuario, producto);
        return favoritoRepository.save(favorito);
    }

    public List<Favorito> listarFavoritos() {
        return favoritoRepository.findAll();
    }
}
