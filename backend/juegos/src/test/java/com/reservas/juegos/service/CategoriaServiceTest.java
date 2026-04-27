package com.reservas.juegos.service;

import com.reservas.juegos.dto.CategoriaDTO;
import com.reservas.juegos.entities.Categoria;
import com.reservas.juegos.repository.CategoriaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CategoriaService - Pruebas unitarias")
class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaService categoriaService;

    private Categoria categoria1;
    private Categoria categoria2;
    private CategoriaDTO dto;

    @BeforeEach
    void setUp() {
        categoria1 = new Categoria("Acción", "⚔️", 520);
        categoria1.setId(1L);

        categoria2 = new Categoria("Aventura", "🌍", 412);
        categoria2.setId(2L);

        dto = new CategoriaDTO("RPG", "🐉", 387);
    }

    @Test
    @DisplayName("listarTodas() debe retornar la lista completa de categorías")
    void listarTodas_debeRetornarTodasLasCategorias() {
        when(categoriaRepository.findAll()).thenReturn(Arrays.asList(categoria1, categoria2));

        List<Categoria> resultado = categoriaService.listarTodas();

        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getNombre()).isEqualTo("Acción");
        assertThat(resultado.get(1).getNombre()).isEqualTo("Aventura");
        verify(categoriaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("buscarPorId() debe retornar la categoría si existe")
    void buscarPorId_categoriaExistente_debeRetornarCategoria() {
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria1));

        Optional<Categoria> resultado = categoriaService.buscarPorId(1L);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNombre()).isEqualTo("Acción");
    }

    @Test
    @DisplayName("buscarPorId() debe retornar vacío si no existe")
    void buscarPorId_categoriaInexistente_debeRetornarVacio() {
        when(categoriaRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Categoria> resultado = categoriaService.buscarPorId(99L);

        assertThat(resultado).isEmpty();
    }

    @Test
    @DisplayName("crear() debe guardar y retornar la nueva categoría")
    void crear_debeGuardarYRetornarCategoria() {
        Categoria guardada = new Categoria("RPG", "🐉", 387);
        guardada.setId(3L);
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(guardada);

        Categoria resultado = categoriaService.crear(dto);

        assertThat(resultado.getNombre()).isEqualTo("RPG");
        assertThat(resultado.getEmoji()).isEqualTo("🐉");
        assertThat(resultado.getCantidadJuegos()).isEqualTo(387);
        verify(categoriaRepository, times(1)).save(any(Categoria.class));
    }

    @Test
    @DisplayName("actualizar() debe modificar los datos si la categoría existe")
    void actualizar_categoriaExistente_debeActualizarDatos() {
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria1));
        when(categoriaRepository.save(any(Categoria.class))).thenAnswer(inv -> inv.getArgument(0));

        CategoriaDTO nuevoDto = new CategoriaDTO("Acción Actualizada", "⚔️", 600);
        Optional<Categoria> resultado = categoriaService.actualizar(1L, nuevoDto);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNombre()).isEqualTo("Acción Actualizada");
        assertThat(resultado.get().getCantidadJuegos()).isEqualTo(600);
    }

    @Test
    @DisplayName("actualizar() debe retornar vacío si la categoría no existe")
    void actualizar_categoriaInexistente_debeRetornarVacio() {
        when(categoriaRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Categoria> resultado = categoriaService.actualizar(99L, dto);

        assertThat(resultado).isEmpty();
        verify(categoriaRepository, never()).save(any());
    }

    @Test
    @DisplayName("eliminar() debe retornar true y borrar si la categoría existe")
    void eliminar_categoriaExistente_debeRetornarTrue() {
        when(categoriaRepository.existsById(1L)).thenReturn(true);

        boolean resultado = categoriaService.eliminar(1L);

        assertThat(resultado).isTrue();
        verify(categoriaRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("eliminar() debe retornar false si la categoría no existe")
    void eliminar_categoriaInexistente_debeRetornarFalse() {
        when(categoriaRepository.existsById(99L)).thenReturn(false);

        boolean resultado = categoriaService.eliminar(99L);

        assertThat(resultado).isFalse();
        verify(categoriaRepository, never()).deleteById(any());
    }
}
