package com.example.proyecto.Service;

import com.example.proyecto.Model.Usuario;
import com.example.proyecto.Repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public Usuario findById(Integer id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    public Usuario save(Usuario usuario) {
        String encodedPassword = passwordEncoder.encode(usuario.getContrasenia());
        usuario.setContrasenia(encodedPassword);
        return usuarioRepository.save(usuario);
    }

    public void delete(Usuario usuario) {
        usuarioRepository.delete(usuario);
    }

    public Optional<Usuario> findByEmail(String email) {
            return usuarioRepository.findByEmail(email);
    }

    public boolean actualizarPassword(Integer id, String nuevaPassword) {
        return usuarioRepository.findById(id).map(u -> {
            u.setContrasenia(passwordEncoder.encode(nuevaPassword));
            usuarioRepository.save(u);
            return true;
        }).orElse(false);
    }

  @Transactional // Esta anotación a nivel de método es vital
    public boolean darDeBaja(Integer id) {
        return usuarioRepository.findById(id).map(u -> {
            u.setEstado("INACTIVO");
            usuarioRepository.saveAndFlush(u); // saveAndFlush fuerza la escritura inmediata
            System.out.println("DEBUG: Usuario " + id + " cambiado a INACTIVO");
            return true;
        }).orElse(false);
    }

}
