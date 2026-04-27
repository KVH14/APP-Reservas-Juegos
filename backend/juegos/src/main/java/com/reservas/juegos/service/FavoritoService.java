package com.reservas.juegos.service;

import com.reservas.juegos.dto.FavoritoDTO;
import com.reservas.juegos.entities.Favorito;
import com.reservas.juegos.entities.Usuario;
import com.reservas.juegos.entities.Producto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FavoritoService {
    private final List<Favorito> favoritos = new ArrayList<>();
    private static Long contador = 1L;

    private final UsuarioService usuarioService;
    private final ProductoService productoService;

    public FavoritoService(UsuarioService usuarioService, ProductoService productoService) {
        this.usuarioService = usuarioService;
        this.productoService = productoService;
    }

    public Favorito crearFavorito(FavoritoDTO dto) {
        Usuario usuario = usuarioService.obtenerUsuario(dto.getUsuarioId());
        Producto producto = productoService.buscarPorId(dto.getProductoId()).orElse(null);

        if (usuario == null || producto == null) {
            throw new IllegalArgumentException("Usuario o producto no encontrado");
        }

        Favorito favorito = new Favorito(contador++, usuario, producto);
        favoritos.add(favorito);
        return favorito;
    }

    public List<Favorito> listarFavoritos() {
        return favoritos;
    }
}
