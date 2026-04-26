package com.reservas.juegos.service;

import com.reservas.juegos.dto.CategoriaDTO;
import com.reservas.juegos.entities.Categoria;
import com.reservas.juegos.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Categoria> listarTodas() {
        return categoriaRepository.findAll();
    }

    public Optional<Categoria> buscarPorId(Long id) {
        return categoriaRepository.findById(id);
    }

    public Categoria crear(CategoriaDTO dto) {
        Categoria categoria = new Categoria(
                dto.getNombre(),
                dto.getEmoji(),
                dto.getCantidadJuegos()
        );
        return categoriaRepository.save(categoria);
    }

    public Optional<Categoria> actualizar(Long id, CategoriaDTO dto) {
        return categoriaRepository.findById(id).map(cat -> {
            cat.setNombre(dto.getNombre());
            cat.setEmoji(dto.getEmoji());
            cat.setCantidadJuegos(dto.getCantidadJuegos());
            return categoriaRepository.save(cat);
        });
    }

    public boolean eliminar(Long id) {
        if (!categoriaRepository.existsById(id)) {
            return false;
        }
        categoriaRepository.deleteById(id);
        return true;
    }

    public boolean existePorNombre(String nombre) {
        return categoriaRepository.existsByNombre(nombre);
    }
}
