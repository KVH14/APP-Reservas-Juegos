package com.reservas.juegos.service;
import com.reservas.juegos.dto.ReservaDTO;
import com.reservas.juegos.entities.Producto;
import com.reservas.juegos.entities.Reserva;
import com.reservas.juegos.repository.ProductoRepository;
import com.reservas.juegos.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.reservas.juegos.factory.ReservaFactory;

import java.util.List;
import java.util.Optional;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    public List<Reserva> listarTodas() {
        return reservaRepository.findAll();
    }

    public Optional<Reserva> buscarPorId(Long id) {
        return reservaRepository.findById(id);
    }

    public List<Reserva> buscarPorEmail(String email) {
        return reservaRepository.findByEmailCliente(email);
    }

    public List<Reserva> buscarPorProducto(Long productoId) {
        return reservaRepository.findByProductoId(productoId);
    }

    // #32 – Realizar reserva
    public Optional<Reserva> crear(ReservaDTO dto) {
        if (dto.getNombreCliente() == null || dto.getNombreCliente().isBlank()) return Optional.empty();
        if (dto.getEmailCliente() == null || dto.getEmailCliente().isBlank()) return Optional.empty();
        if (dto.getFechaReserva() == null) return Optional.empty();

        Optional<Producto> productoOpt = productoRepository.findById(dto.getProductoId());
        if (productoOpt.isEmpty()) return Optional.empty();

        Producto producto = productoOpt.get();

        // Verificar que el producto tenga stock disponible
        if (producto.getStock() <= 0) return Optional.empty();

        // Descontar stock
        producto.setStock(producto.getStock() - 1);
        productoRepository.save(producto);

        Reserva reserva = ReservaFactory.crearReservaConFecha(
                dto.getNombreCliente(),
                dto.getEmailCliente(),
                producto,
                dto.getFechaReserva(),
                dto.getFechaDevolucion()
        );

        return Optional.of(reservaRepository.save(reserva));
    }

    // Cancelar reserva (devuelve stock)
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

    // Confirmar reserva
    public Optional<Reserva> confirmar(Long id) {
        return reservaRepository.findById(id).map(r -> {
            r.setEstado("CONFIRMADA");
            return reservaRepository.save(r);
        });
    }

    public boolean eliminar(Long id) {
        if (!reservaRepository.existsById(id)) return false;
        reservaRepository.deleteById(id);
        return true;
    }
}
