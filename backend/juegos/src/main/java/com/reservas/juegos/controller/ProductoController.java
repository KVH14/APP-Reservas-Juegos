package com.reservas.juegos.controller;

import com.reservas.juegos.entities.Caracteristica;
import com.reservas.juegos.entities.Producto;
import com.reservas.juegos.service.CaracteristicaService;
import com.reservas.juegos.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * Endpoints:
 *   GET    /api/productos                          → listar todos
 *   GET    /api/productos/{id}                     → buscar por id
 *   POST   /api/productos                          → crear producto
 *   PUT    /api/productos/{id}                     → actualizar producto
 *   DELETE /api/productos/{id}                     → eliminar producto
 *   POST   /api/productos/{id}/categorias/{catId}  → #12 asignar categoría
 *   DELETE /api/productos/{id}/categorias/{catId}  → quitar categoría
 *   GET    /api/productos/{id}/caracteristicas     → visualizar características
 */
@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private CaracteristicaService caracteristicaService;

    // Listar todos los productos (con paginación opcional)
    @GetMapping
    public ResponseEntity<?> listarTodos(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {

        if (page == null || size == null) {
            return ResponseEntity.ok(productoService.listarTodos());
        }

        Page<Producto> productosPage = productoService.listarPaginado(page, size);
        Map<String, Object> response = new HashMap<>();
        response.put("contenido", productosPage.getContent());
        response.put("paginaActual", productosPage.getNumber());
        response.put("totalElementos", productosPage.getTotalElements());
        response.put("totalPaginas", productosPage.getTotalPages());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> buscarPorId(@PathVariable Long id) {
        return productoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Producto> crear(@RequestBody Producto producto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.crear(producto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(@PathVariable Long id, @RequestBody Producto datos) {
        return productoService.actualizar(id, datos)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        return productoService.eliminar(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    // #12 Categorizar producto
    @PostMapping("/{id}/categorias/{categoriaId}")
    public ResponseEntity<Producto> asignarCategoria(
            @PathVariable Long id,
            @PathVariable Long categoriaId) {
        return productoService.asignarCategoria(id, categoriaId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}/categorias/{categoriaId}")
    public ResponseEntity<Producto> quitarCategoria(
            @PathVariable Long id,
            @PathVariable Long categoriaId) {
        return productoService.quitarCategoria(id, categoriaId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Visualizar características del producto
    @GetMapping("/{id}/caracteristicas")
    public ResponseEntity<List<Caracteristica>> verCaracteristicas(@PathVariable Long id) {
        return caracteristicaService.verCaracteristicasDeProducto(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
