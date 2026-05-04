package com.reservas.juegos.service;

import com.reservas.juegos.entities.Usuario;
import com.reservas.juegos.repository.UsuarioRepository;
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

    // admin hardcodeado
    private static final String ADMIN_EMAIL    = "admin@playres.com";
    private static final String ADMIN_PASSWORD = "Admin123!";

    //Registra un nuevo usuario La contraseña se guarda hasheada con BCrypt.

    public Usuario registrar(String email, String password, String nombre) throws Exception {
        if (usuarioRepository.findByEmail(email).isPresent()) {
            throw new Exception("Email ya existe");
        }
        String hash = passwordEncoder.encode(password);
        Usuario usuario = new Usuario(email, hash, nombre, "USUARIO");
        return usuarioRepository.save(usuario);
    }

    //Valida credenciales comparando con el hash BCrypt almacenado.

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

    public boolean esAdmin(String email, String password) {
        return ADMIN_EMAIL.equals(email) && ADMIN_PASSWORD.equals(password);
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
}