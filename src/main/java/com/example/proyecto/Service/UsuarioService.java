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
        // CAMBIO CLAVE: Comprobamos si el ID es distinto de null
        if (usuario.getId() != null) { // Es una edición (ya tiene un ID asignado)
            Usuario usuarioExistente = usuarioRepository.findById(usuario.getId()).orElse(null);

            // Si el admin dejó la contraseña vacía en el formulario
            if (usuario.getContrasenia() == null || usuario.getContrasenia().isEmpty()) {
                if (usuarioExistente != null) {
                    // Mantenemos el hash que ya teníamos guardado
                    usuario.setContrasenia(usuarioExistente.getContrasenia());
                }
            } else {
                // Solo encriptamos si el admin escribió una nueva contraseña
                usuario.setContrasenia(passwordEncoder.encode(usuario.getContrasenia()));
            }
        } else {
            // Es un usuario nuevo (ID es null), encriptamos obligatoriamente
            usuario.setContrasenia(passwordEncoder.encode(usuario.getContrasenia()));
        }
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

    // En UsuarioService.java
    @Transactional
    public Usuario actualizarDatosSinPassword(Usuario detallesNuevos) {
        Usuario usuarioBD = usuarioRepository.findById(detallesNuevos.getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Actualizamos solo los campos de perfil
        usuarioBD.setNombre(detallesNuevos.getNombre());
        usuarioBD.setApellidos(detallesNuevos.getApellidos());
        usuarioBD.setTelefono(detallesNuevos.getTelefono());
        usuarioBD.setDireccion(detallesNuevos.getDireccion());
        usuarioBD.setCiudad(detallesNuevos.getCiudad());
        usuarioBD.setCodigoPostal(detallesNuevos.getCodigoPostal());
        // NO tocamos usuarioBD.setContrasenia()

        return usuarioRepository.save(usuarioBD);
    }

}
