package com.reservas.juegos.service;

import com.reservas.juegos.entities.Usuario;
import com.reservas.juegos.repository.UsuarioRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void inicializarAdmin() {
        if (usuarioRepository.findByEmail("admin@playres.com").isEmpty()) {
            String hash = passwordEncoder.encode("Admin123!");
            usuarioRepository.save(new Usuario("admin@playres.com", hash, "Administrador", "ADMIN"));
        }
    }

    public Usuario registrar(String email, String password, String nombre) throws Exception {
        if (usuarioRepository.findByEmail(email).isPresent()) {
            throw new Exception("Email ya existe");
        }
        String hash = passwordEncoder.encode(password);
        return usuarioRepository.save(new Usuario(email, hash, nombre, "USUARIO"));
    }

    public Usuario login(String email, String password) throws Exception {
        Optional<Usuario> opt = usuarioRepository.findByEmail(email);
        if (opt.isEmpty()) {
            throw new Exception("Usuario no encontrado");
        }
        if (!passwordEncoder.matches(password, opt.get().getPassword())) {
            throw new Exception("Contraseña incorrecta");
        }
        return opt.get();
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario obtenerUsuario(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    public Usuario cambiarRol(Long id, String nuevoRol) {
        return usuarioRepository.findById(id).map(u -> {
            u.setRol(nuevoRol.toUpperCase());
            return usuarioRepository.save(u);
        }).orElse(null);
    }
}