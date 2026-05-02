package com.reservas.juegos.service;

import com.reservas.juegos.dto.ReservaDTO;
import com.reservas.juegos.entities.Producto;
import com.reservas.juegos.entities.Reserva;
import com.reservas.juegos.repository.ProductoRepository;
import com.reservas.juegos.repository.ReservaRepository;
import com.reservas.juegos.factory.ReservaFactory;
import com.reservas.juegos.strategy.ReservaNormalStrategy;
import com.reservas.juegos.strategy.ReservaPremiumStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReservaService {

    @Autowired
    private ReservaNormalStrategy normal;

    @Autowired
    private ReservaPremiumStrategy premium;

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    /** Listar todas las reservas */
    public List<Reserva> listarTodas() {
        return reservaRepository.findAll();
    }

    /** Listar reservas sin fecha de devolución */
    public List<Reserva> listarSinFecha() {
        return reservaRepository.findByFechaDevolucionIsNull();
    }

    /** Listar reservas con fecha de devolución */
    public List<Reserva> listarConFecha() {
        return reservaRepository.findByFechaDevolucionIsNotNull();
    }

    /** Buscar reserva por ID */
    public Optional<Reserva> buscarPorId(Long id) {
        return reservaRepository.findById(id);
    }

    /** Buscar reservas por email del cliente */
    public List<Reserva> buscarPorEmail(String email) {
        return reservaRepository.findByEmailCliente(email);
    }

    /** Buscar reservas por producto */
    public List<Reserva> buscarPorProducto(Long productoId) {
        return reservaRepository.findByProductoId(productoId);
    }

    /** Crear nueva reserva (#32 – Realizar reserva) */
    public Optional<Reserva> crear(ReservaDTO dto) {
        if (dto.getNombreCliente() == null || dto.getNombreCliente().isBlank()) return Optional.empty();
        if (dto.getEmailCliente() == null || dto.getEmailCliente().isBlank()) return Optional.empty();
        if (dto.getFechaReserva() == null) return Optional.empty();

        Optional<Producto> productoOpt = productoRepository.findById(dto.getProductoId());
        if (productoOpt.isEmpty()) return Optional.empty();

        Producto producto = productoOpt.get();

        // Verificar stock disponible
        if (producto.getStock() <= 0) return Optional.empty();

        // Descontar stock
        producto.setStock(producto.getStock() - 1);
        productoRepository.save(producto);

        Reserva reserva;

        // Usar estrategia según tipo
        if ("PREMIUM".equalsIgnoreCase(dto.getTipo())) {
            reserva = premium.crear(dto, producto);
        } else {
            reserva = normal.crear(dto, producto);
        }

        return Optional.of(reservaRepository.save(reserva));
    }

    /** Cancelar reserva (devuelve stock) */
    public Optional<Reserva> cancelar(Long id) {
        return reservaRepository.findById(id).map(r -> {
            if ("CANCELADA".equals(r.getEstado())) return r;

            r.setEstado("CANCELADA");

            // Devolver stock al producto
            Producto producto = r.getProducto();
            producto.setStock(producto.getStock() + 1);
            productoRepository.save(producto);

            return reservaRepository.save(r);
        });
    }

    /** Confirmar reserva */
    public Optional<Reserva> confirmar(Long id) {
        return reservaRepository.findById(id).map(r -> {
            r.setEstado("CONFIRMADA");
            return reservaRepository.save(r);
        });
    }

    /** Eliminar reserva */
    public boolean eliminar(Long id) {
        if (!reservaRepository.existsById(id)) return false;
        reservaRepository.deleteById(id);
        return true;
    }
}
