package com.reservas.juegos.service;

import com.reservas.juegos.entities.Usuario;
import com.reservas.juegos.dto.UsuarioDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UsuarioService {
    private final List<Usuario> usuarios = new ArrayList<>();
    private static Long contador = 1L;

    public Usuario registrarUsuario(UsuarioDTO dto) {
        Usuario usuario = new Usuario(contador++, dto.getNombre(), dto.getCorreo());
        usuarios.add(usuario);
        return usuario;
    }

    public List<Usuario> listarUsuarios() {
        return usuarios;
    }

    public Usuario obtenerUsuario(Long id) {
        return usuarios.stream().filter(u -> u.getId().equals(id)).findFirst().orElse(null);
    }
}
