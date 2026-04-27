package com.reservas.juegos.service;

import com.reservas.juegos.entities.Reserva;
import com.reservas.juegos.entities.Usuario;
import com.reservas.juegos.entities.Producto;
import com.reservas.juegos.dto.ReservaDTO;
import com.reservas.juegos.factory.ReservaFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReservaService {
    private final List<Reserva> reservas = new ArrayList<>();
    private static Long contador = 1L;

    private final UsuarioService usuarioService;
    private final ProductoService productoService;

    public ReservaService(UsuarioService usuarioService, ProductoService productoService) {
        this.usuarioService = usuarioService;
        this.productoService = productoService;
    }

    public Reserva crearReserva(ReservaDTO dto) {
        Usuario usuario = usuarioService.obtenerUsuario(dto.getUsuarioId());
        Producto producto = productoService.obtenerProducto(dto.getProductoId());

        if (usuario == null || producto == null) {
            throw new IllegalArgumentException("Usuario o producto no válido.");
        }

        Reserva reserva = ReservaFactory.crearReserva(contador++, dto.getFecha(), usuario, producto);
        reservas.add(reserva);
        return reserva;
    }

    public List<Reserva> listarReservas() {
        return reservas;
    }

    public Reserva obtenerReserva(Long id) {
        return reservas.stream().filter(r -> r.getId().equals(id)).findFirst().orElse(null);
    }
}
