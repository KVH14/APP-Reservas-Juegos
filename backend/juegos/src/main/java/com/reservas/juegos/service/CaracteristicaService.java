package com.reservas.juegos.service;

import com.reservas.juegos.dto.CaracteristicaDTO;
import com.reservas.juegos.entities.Caracteristica;
import com.reservas.juegos.entities.Producto;
import com.reservas.juegos.repository.CaracteristicaRepository;
import com.reservas.juegos.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CaracteristicaService {

    @Autowired
    private CaracteristicaRepository caracteristicaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    public List<Caracteristica> listarPorProducto(Long productoId) {
        return caracteristicaRepository.findByProductoId(productoId);
    }

    public List<Caracteristica> listarTodas() {
        return caracteristicaRepository.findAll();
    }

    public Optional<Caracteristica> buscarPorId(Long id) {
        return caracteristicaRepository.findById(id);
    }

    public Optional<Caracteristica> crear(CaracteristicaDTO dto) {
        return productoRepository.findById(dto.getProductoId()).map(producto -> {
            Caracteristica c = new Caracteristica(dto.getClave(), dto.getValor(), producto);
            return caracteristicaRepository.save(c);
        });
    }

    public Optional<Caracteristica> actualizar(Long id, CaracteristicaDTO dto) {
        return caracteristicaRepository.findById(id).map(c -> {
            c.setClave(dto.getClave());
            c.setValor(dto.getValor());
            return caracteristicaRepository.save(c);
        });
    }

    public boolean eliminar(Long id) {
        if (!caracteristicaRepository.existsById(id)) {
            return false;
        }
        caracteristicaRepository.deleteById(id);
        return true;
    }

    // Visualizar todas las características de un producto (para product-detail)
    public Optional<List<Caracteristica>> verCaracteristicasDeProducto(Long productoId) {
        if (!productoRepository.existsById(productoId)) {
            return Optional.empty();
        }
        return Optional.of(caracteristicaRepository.findByProductoId(productoId));
    }
}
