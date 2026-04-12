package com.example.proyecto.Service;

import com.example.proyecto.Model.Usuario;
import com.example.proyecto.Repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void alGuardarUsuarioLaContraseniaDebeEncriptarse() {

        Usuario usuario = new Usuario();
        usuario.setContrasenia("12345");
        usuario.setEmail("test@test.com");


        when(passwordEncoder.encode("12345")).thenReturn("encriptado_hash");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(i -> i.getArguments()[0]);


        Usuario usuarioGuardado = usuarioService.save(usuario);

        verify(passwordEncoder, times(1)).encode("12345");
        assertEquals("encriptado_hash", usuarioGuardado.getContrasenia());
        assertNotEquals("12345", usuarioGuardado.getContrasenia());
    }
}