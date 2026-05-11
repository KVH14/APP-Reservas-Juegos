package com.reservas.juegos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reservas.juegos.dto.CategoriaDTO;
import com.reservas.juegos.entities.Categoria;
import com.reservas.juegos.service.CategoriaService;
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

@WebMvcTest(CategoriaController.class)
@DisplayName("CategoriaController - Pruebas de integración Web")
class CategoriaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoriaService categoriaService;

    @Autowired
    private ObjectMapper objectMapper;

    private Categoria categoria1;
    private Categoria categoria2;

    @BeforeEach
    void setUp() {
        categoria1 = new Categoria("Acción", "⚔️", 520);
        categoria1.setId(1L);

        categoria2 = new Categoria("Aventura", "🌍", 412);
        categoria2.setId(2L);
    }

    @Test
    @DisplayName("GET /api/categorias debe retornar 200 con lista de categorías")
    void listarTodas_debeRetornar200ConLista() throws Exception {
        when(categoriaService.listarTodas()).thenReturn(Arrays.asList(categoria1, categoria2));

        mockMvc.perform(get("/api/categorias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nombre").value("Acción"))
                .andExpect(jsonPath("$[1].nombre").value("Aventura"));
    }

    @Test
    @DisplayName("GET /api/categorias/{id} debe retornar 200 si existe")
    void buscarPorId_existente_debeRetornar200() throws Exception {
        when(categoriaService.buscarPorId(1L)).thenReturn(Optional.of(categoria1));

        mockMvc.perform(get("/api/categorias/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Acción"))
                .andExpect(jsonPath("$.emoji").value("⚔️"));
    }

    @Test
    @DisplayName("GET /api/categorias/{id} debe retornar 404 si no existe")
    void buscarPorId_inexistente_debeRetornar404() throws Exception {
        when(categoriaService.buscarPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/categorias/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/categorias debe retornar 201 con la categoría creada")
    void agregar_datosValidos_debeRetornar201() throws Exception {
        CategoriaDTO dto = new CategoriaDTO("RPG", "🐉", 387);
        Categoria guardada = new Categoria("RPG", "🐉", 387);
        guardada.setId(3L);
        when(categoriaService.crear(any(CategoriaDTO.class))).thenReturn(guardada);

        mockMvc.perform(post("/api/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("RPG"))
                .andExpect(jsonPath("$.id").value(3));
    }

    @Test
    @DisplayName("POST /api/categorias debe retornar 400 si el nombre está vacío")
    void agregar_nombreVacio_debeRetornar400() throws Exception {
        CategoriaDTO dto = new CategoriaDTO("", "🐉", 0);

        mockMvc.perform(post("/api/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("DELETE /api/categorias/{id} debe retornar 204 si existe")
    void eliminar_existente_debeRetornar204() throws Exception {
        when(categoriaService.eliminar(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/categorias/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/categorias/{id} debe retornar 404 si no existe")
    void eliminar_inexistente_debeRetornar404() throws Exception {
        when(categoriaService.eliminar(99L)).thenReturn(false);

        mockMvc.perform(delete("/api/categorias/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /api/categorias/{id} debe retornar 200 con datos actualizados")
    void actualizar_existente_debeRetornar200() throws Exception {
        CategoriaDTO dto = new CategoriaDTO("Acción Premium", "⚔️", 600);
        Categoria actualizada = new Categoria("Acción Premium", "⚔️", 600);
        actualizada.setId(1L);
        when(categoriaService.actualizar(eq(1L), any(CategoriaDTO.class))).thenReturn(Optional.of(actualizada));

        mockMvc.perform(put("/api/categorias/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Acción Premium"))
                .andExpect(jsonPath("$.cantidadJuegos").value(600));
    }
}
