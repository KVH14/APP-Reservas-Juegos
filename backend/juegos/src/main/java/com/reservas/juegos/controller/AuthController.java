package com.reservas.juegos.controller;

import com.reservas.juegos.entities.Usuario;
import com.reservas.juegos.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    // Registro de usuario
    @PostMapping("/registro")
    public ResponseEntity<?> registro(@RequestBody Map<String, String> body) {
        try {
            String email = body.get("email");
            String password = body.get("password");
            String nombre = body.get("nombre");

            Usuario usuario = usuarioService.registrar(email, password, nombre);
            return ResponseEntity.ok(usuario);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Login de usuario
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        try {
            String email = body.get("email");
            String password = body.get("password");

            // Verificar si es admin
            if (usuarioService.esAdmin(email, password)) {
                return ResponseEntity.ok(Map.of(
                        "id", 0L,
                        "email", email,
                        "nombre", "Administrador",
                        "rol", "ADMIN"
                ));
            }

            Usuario usuario = usuarioService.login(email, password);
            return ResponseEntity.ok(usuario);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Logut de usuario
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok(Map.of("mensaje", "Sesión cerrada"));
    }

    // Verificar si el usuario es admin
    @GetMapping("/verificar-admin")
    public ResponseEntity<?> verificarAdmin(@RequestParam String email, @RequestParam String password) {
        boolean esAdmin = usuarioService.esAdmin(email, password);
        return ResponseEntity.ok(Map.of("esAdmin", esAdmin));
    }
}
