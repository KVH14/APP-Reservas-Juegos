package com.reservas.juegos.service;

import com.reservas.juegos.dto.CaracteristicaDTO;
import com.reservas.juegos.entities.Caracteristica;
import com.reservas.juegos.entities.Producto;
import com.reservas.juegos.repository.CaracteristicaRepository;
import com.reservas.juegos.repository.ProductoRepository;
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
@DisplayName("CaracteristicaService - Pruebas unitarias")
class CaracteristicaServiceTest {

    @Mock
    private CaracteristicaRepository caracteristicaRepository;

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private CaracteristicaService caracteristicaService;

    private Producto producto;
    private Caracteristica c1;
    private Caracteristica c2;
    private CaracteristicaDTO dto;

    @BeforeEach
    void setUp() {
        producto = new Producto("Elden Ring", "PS5", "RPG", 9900, 50, "disponible", 4.9, "🌌");
        producto.setId(1L);

        c1 = new Caracteristica("Desarrollador", "FromSoftware", producto);
        c1.setId(1L);

        c2 = new Caracteristica("Año", "2024", producto);
        c2.setId(2L);

        dto = new CaracteristicaDTO("Jugadores", "1-4", 1L);
    }

    @Test
    @DisplayName("listarPorProducto() debe retornar las características del producto")
    void listarPorProducto_debeRetornarCaracteristicas() {
        when(caracteristicaRepository.findByProductoId(1L)).thenReturn(Arrays.asList(c1, c2));

        List<Caracteristica> resultado = caracteristicaService.listarPorProducto(1L);

        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getClave()).isEqualTo("Desarrollador");
        assertThat(resultado.get(1).getClave()).isEqualTo("Año");
    }

    @Test
    @DisplayName("crear() debe guardar la característica si el producto existe")
    void crear_productoExistente_debeGuardarCaracteristica() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        Caracteristica guardada = new Caracteristica("Jugadores", "1-4", producto);
        guardada.setId(3L);
        when(caracteristicaRepository.save(any(Caracteristica.class))).thenReturn(guardada);

        Optional<Caracteristica> resultado = caracteristicaService.crear(dto);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getClave()).isEqualTo("Jugadores");
        assertThat(resultado.get().getValor()).isEqualTo("1-4");
        verify(caracteristicaRepository, times(1)).save(any(Caracteristica.class));
    }

    @Test
    @DisplayName("crear() debe retornar vacío si el producto no existe")
    void crear_productoInexistente_debeRetornarVacio() {
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());
        dto.setProductoId(99L);

        Optional<Caracteristica> resultado = caracteristicaService.crear(dto);

        assertThat(resultado).isEmpty();
        verify(caracteristicaRepository, never()).save(any());
    }

    @Test
    @DisplayName("actualizar() debe modificar clave y valor si existe")
    void actualizar_existente_debeModificar() {
        when(caracteristicaRepository.findById(1L)).thenReturn(Optional.of(c1));
        when(caracteristicaRepository.save(any(Caracteristica.class))).thenAnswer(inv -> inv.getArgument(0));

        CaracteristicaDTO actualizacion = new CaracteristicaDTO("Desarrollador", "Bandai Namco", 1L);
        Optional<Caracteristica> resultado = caracteristicaService.actualizar(1L, actualizacion);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getValor()).isEqualTo("Bandai Namco");
    }

    @Test
    @DisplayName("eliminar() debe retornar true y borrar si existe")
    void eliminar_existente_debeRetornarTrue() {
        when(caracteristicaRepository.existsById(1L)).thenReturn(true);

        boolean resultado = caracteristicaService.eliminar(1L);

        assertThat(resultado).isTrue();
        verify(caracteristicaRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("eliminar() debe retornar false si no existe")
    void eliminar_inexistente_debeRetornarFalse() {
        when(caracteristicaRepository.existsById(99L)).thenReturn(false);

        boolean resultado = caracteristicaService.eliminar(99L);

        assertThat(resultado).isFalse();
        verify(caracteristicaRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("verCaracteristicasDeProducto() debe retornar lista si el producto existe")
    void verCaracteristicasDeProducto_productoExistente_debeRetornarLista() {
        when(productoRepository.existsById(1L)).thenReturn(true);
        when(caracteristicaRepository.findByProductoId(1L)).thenReturn(Arrays.asList(c1, c2));

        Optional<List<Caracteristica>> resultado = caracteristicaService.verCaracteristicasDeProducto(1L);

        assertThat(resultado).isPresent();
        assertThat(resultado.get()).hasSize(2);
    }

    @Test
    @DisplayName("verCaracteristicasDeProducto() debe retornar vacío si el producto no existe")
    void verCaracteristicasDeProducto_productoInexistente_debeRetornarVacio() {
        when(productoRepository.existsById(99L)).thenReturn(false);

        Optional<List<Caracteristica>> resultado = caracteristicaService.verCaracteristicasDeProducto(99L);

        assertThat(resultado).isEmpty();
    }
}
