package com.reservas.juegos.controller;

import com.reservas.juegos.dto.ProductoDTO;
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
 *   POST   /api/productos                          → crear producto        → 201 + producto creado
 *   PUT    /api/productos/{id}                     → actualizar producto   → 200 + producto actualizado
 *   DELETE /api/productos/{id}                     → eliminar producto     → 200 + mensaje confirmación
 *   POST   /api/productos/{id}/categorias/{catId}  → asignar categoría     → 200 + producto
 *   DELETE /api/productos/{id}/categorias/{catId}  → quitar categoría      → 200 + producto
 *   GET    /api/productos/{id}/caracteristicas     → ver características
 *   GET    /api/productos/{id}/politicas           → ver políticas
 *   GET    /api/productos/{id}/compartir           → datos para compartir
 *   POST   /api/productos/{id}/puntuar             → puntuar producto
 *   POST   /api/productos/importarRawg             → importar juego desde RAWG
 */
@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private CaracteristicaService caracteristicaService;

    // ── GET /api/productos ──────────────────────────────────────────────────────
    // Listar todos (con paginación opcional via ?page=0&size=10)
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

    // ── GET /api/productos/{id} ─────────────────────────────────────────────────
    @GetMapping("/{id}")
    public ResponseEntity<Producto> buscarPorId(@PathVariable Long id) {
        return productoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ── POST /api/productos ─────────────────────────────────────────────────────
    // Recibe Producto directo (sin DTO), devuelve 201 + producto creado
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Producto producto) {
        try {
            Producto creado = productoService.crear(producto);
            return ResponseEntity.status(HttpStatus.CREATED).body(creado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ── PUT /api/productos/{id} ─────────────────────────────────────────────────
    // Recibe Producto directo, devuelve 200 + producto actualizado
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(
            @PathVariable Long id,
            @RequestBody Producto datos) {
        return productoService.actualizar(id, datos)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ── DELETE /api/productos/{id} ──────────────────────────────────────────────
    // Devuelve 200 + mensaje (en vez de 204 sin body, para que Postman muestre respuesta)
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> eliminar(@PathVariable Long id) {
        if (productoService.eliminar(id)) {
            return ResponseEntity.ok(Map.of("mensaje", "Producto eliminado correctamente"));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Producto no encontrado con id: " + id));
    }

    // ── POST /api/productos/{id}/categorias/{categoriaId} ──────────────────────
    // #12 Asignar categoría a producto
    @PostMapping("/{id}/categorias/{categoriaId}")
    public ResponseEntity<Producto> asignarCategoria(
            @PathVariable("id") Long productoId,
            @PathVariable Long categoriaId) {
        return productoService.asignarCategoria(productoId, categoriaId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ── DELETE /api/productos/{id}/categorias/{categoriaId} ────────────────────
    // Quitar categoría de producto → devuelve 200 + producto actualizado
    @DeleteMapping("/{id}/categorias/{categoriaId}")
    public ResponseEntity<Producto> quitarCategoria(
            @PathVariable("id") Long productoId,
            @PathVariable Long categoriaId) {
        return productoService.quitarCategoria(productoId, categoriaId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ── GET /api/productos/{id}/caracteristicas ─────────────────────────────────
    @GetMapping("/{id}/caracteristicas")
    public ResponseEntity<List<Caracteristica>> verCaracteristicas(@PathVariable Long id) {
        return caracteristicaService.verCaracteristicasDeProducto(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ── GET /api/productos/{id}/politicas ───────────────────────────────────────
    // #26 Ver bloque de políticas del producto
    @GetMapping("/{id}/politicas")
    public ResponseEntity<?> verPoliticas(@PathVariable Long id) {
        return productoService.obtenerPoliticas(id)
                .map(pol -> ResponseEntity.ok((Object) Map.of("politicas", pol != null ? pol : "")))
                .orElse(ResponseEntity.notFound().build());
    }

    // ── GET /api/productos/{id}/compartir ───────────────────────────────────────
    // #27 Compartir producto en redes sociales
    @GetMapping("/{id}/compartir")
    public ResponseEntity<Map<String, String>> compartir(@PathVariable Long id) {
        return productoService.obtenerDatosCompartir(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ── POST /api/productos/{id}/puntuar ────────────────────────────────────────
    // #28 Puntuar producto → body: { "puntuacion": 4.5 }
    @PostMapping("/{id}/puntuar")
    public ResponseEntity<?> puntuar(
            @PathVariable Long id,
            @RequestBody Map<String, Double> body) {

        Double puntuacion = body.get("puntuacion");
        if (puntuacion == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "El campo 'puntuacion' es obligatorio."));
        }
        if (puntuacion < 1 || puntuacion > 5) {
            return ResponseEntity.badRequest().body(Map.of("error", "La puntuación debe estar entre 1 y 5."));
        }

        return productoService.puntuar(id, puntuacion)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ── POST /api/productos/importarRawg ────────────────────────────────────────
    // #38 Importar juego desde RAWG
    // Body esperado: { "rawgId": 3498, "precio": 59.99, "stock": 10, "plataforma": "PC" }
    @PostMapping("/importarRawg")
    public ResponseEntity<?> importarRawg(@RequestBody ProductoDTO dto) {
        try {
            Producto nuevo = productoService.importarDesdeRawg(
                    dto.getRawgId(),
                    dto.getPrecio(),
                    dto.getStock(),
                    dto.getPlataforma()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}