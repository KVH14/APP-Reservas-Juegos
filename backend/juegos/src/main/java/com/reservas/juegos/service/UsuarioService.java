package com.reservas.juegos.service;

import com.reservas.juegos.entities.Usuario;
import com.reservas.juegos.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Admin hardcodeado
    private static final String ADMIN_EMAIL = "admin@playres.com";
    private static final String ADMIN_PASSWORD = "Admin123!";

    public Usuario registrar(String email, String password, String nombre) throws Exception {
        if (usuarioRepository.findByEmail(email).isPresent()) {
            throw new Exception("Email ya existe");
        }
        Usuario usuario = new Usuario(email, password, nombre, "USUARIO");
        return usuarioRepository.save(usuario);
    }

    public Usuario login(String email, String password) throws Exception {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
        if (usuario.isEmpty()) {
            throw new Exception("Usuario no encontrado");
        }
        if (!usuario.get().getPassword().equals(password)) {
            throw new Exception("Contraseña incorrecta");
        }
        return usuario.get();
    }

    public boolean esAdmin(String email, String password) {
        return ADMIN_EMAIL.equals(email) && ADMIN_PASSWORD.equals(password);
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    // Listar todos los usuarios
    public java.util.List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    // Obtener usuario por id (retorna null si no existe - usado por controlador)
    public Usuario obtenerUsuario(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }
}
