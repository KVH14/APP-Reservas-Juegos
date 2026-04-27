package com.reservas.juegos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reservas.juegos.dto.CaracteristicaDTO;
import com.reservas.juegos.entities.Caracteristica;
import com.reservas.juegos.entities.Producto;
import com.reservas.juegos.service.CaracteristicaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CaracteristicaController.class)
@DisplayName("CaracteristicaController - Pruebas de integración Web")
class CaracteristicaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CaracteristicaService caracteristicaService;

    @Autowired
    private ObjectMapper objectMapper;

    private Producto producto;
    private Caracteristica c1;
    private Caracteristica c2;

    @BeforeEach
    void setUp() {
        producto = new Producto("Elden Ring", "PS5", "RPG", 9900, 50, "disponible", 4.9, "🌌");
        producto.setId(1L);

        c1 = new Caracteristica("Desarrollador", "FromSoftware", producto);
        c1.setId(1L);

        c2 = new Caracteristica("Año", "2024", producto);
        c2.setId(2L);
    }

    @Test
    @DisplayName("GET /api/caracteristicas debe retornar 200 con lista")
    void listarTodas_debeRetornar200() throws Exception {
        when(caracteristicaService.listarTodas()).thenReturn(Arrays.asList(c1, c2));

        mockMvc.perform(get("/api/caracteristicas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].clave").value("Desarrollador"))
                .andExpect(jsonPath("$[1].clave").value("Año"));
    }

    @Test
    @DisplayName("GET /api/caracteristicas/{id} debe retornar 200 si existe")
    void buscarPorId_existente_debeRetornar200() throws Exception {
        when(caracteristicaService.buscarPorId(1L)).thenReturn(Optional.of(c1));

        mockMvc.perform(get("/api/caracteristicas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clave").value("Desarrollador"))
                .andExpect(jsonPath("$.valor").value("FromSoftware"));
    }

    @Test
    @DisplayName("GET /api/caracteristicas/{id} debe retornar 404 si no existe")
    void buscarPorId_inexistente_debeRetornar404() throws Exception {
        when(caracteristicaService.buscarPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/caracteristicas/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/caracteristicas debe retornar 201 si el producto existe")
    void agregar_productoExistente_debeRetornar201() throws Exception {
        CaracteristicaDTO dto = new CaracteristicaDTO("Jugadores", "1-4", 1L);
        Caracteristica guardada = new Caracteristica("Jugadores", "1-4", producto);
        guardada.setId(3L);
        when(caracteristicaService.crear(any(CaracteristicaDTO.class))).thenReturn(Optional.of(guardada));

        mockMvc.perform(post("/api/caracteristicas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.clave").value("Jugadores"))
                .andExpect(jsonPath("$.valor").value("1-4"));
    }

    @Test
    @DisplayName("POST /api/caracteristicas debe retornar 400 si clave está vacía")
    void agregar_claveVacia_debeRetornar400() throws Exception {
        CaracteristicaDTO dto = new CaracteristicaDTO("", "FromSoftware", 1L);

        mockMvc.perform(post("/api/caracteristicas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/caracteristicas debe retornar 404 si el producto no existe")
    void agregar_productoInexistente_debeRetornar404() throws Exception {
        CaracteristicaDTO dto = new CaracteristicaDTO("Clave", "Valor", 99L);
        when(caracteristicaService.crear(any(CaracteristicaDTO.class))).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/caracteristicas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/caracteristicas/{id} debe retornar 204 si existe")
    void eliminar_existente_debeRetornar204() throws Exception {
        when(caracteristicaService.eliminar(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/caracteristicas/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/caracteristicas/{id} debe retornar 404 si no existe")
    void eliminar_inexistente_debeRetornar404() throws Exception {
        when(caracteristicaService.eliminar(99L)).thenReturn(false);

        mockMvc.perform(delete("/api/caracteristicas/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /api/caracteristicas/{id} debe retornar 200 con datos actualizados")
    void actualizar_existente_debeRetornar200() throws Exception {
        CaracteristicaDTO dto = new CaracteristicaDTO("Desarrollador", "Bandai Namco", 1L);
        Caracteristica actualizada = new Caracteristica("Desarrollador", "Bandai Namco", producto);
        actualizada.setId(1L);
        when(caracteristicaService.actualizar(eq(1L), any(CaracteristicaDTO.class)))
                .thenReturn(Optional.of(actualizada));

        mockMvc.perform(put("/api/caracteristicas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valor").value("Bandai Namco"));
    }
}
