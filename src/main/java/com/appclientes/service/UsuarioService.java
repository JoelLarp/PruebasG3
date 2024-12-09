package com.appclientes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.appclientes.dto.UsuarioDTO;
import com.appclientes.model.Usuario;
import com.appclientes.repository.UsuarioRepository;


@Service
public class UsuarioService {
	
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Usuario findByEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + email));
    }

    public Usuario registrarUsuario(UsuarioDTO usuarioDTO) {
        validarUsername(usuarioDTO.getNombre());
        validarDni(usuarioDTO.getDni());
        validarEmailUnico(usuarioDTO.getEmail());
        validarPassword(usuarioDTO.getPassword());

        Usuario usuario = new Usuario();
        usuario.setNombre(usuarioDTO.getNombre());
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setPassword(passwordEncoder.encode(usuarioDTO.getPassword()));
        usuario.setDni(usuarioDTO.getDni());

        return usuarioRepository.save(usuario);
    }

    private void validarUsername(String nombre) {
        if (nombre == null || nombre.length() > 30 || nombre.length() < 6) {
            throw new IllegalArgumentException("El username debe tener entre 6 a 30 caracteres");
        }
        if (usuarioRepository.existsByNombre(nombre)) {
            throw new IllegalArgumentException("El username ya está en uso.");
        }
    }
    
    private void validarDni(String dni) {
        if (dni == null || !dni.matches("\\d{8}")) {
            throw new IllegalArgumentException("El DNI debe contener solo números y tener hasta 8 caracteres.");
        }
        if (usuarioRepository.existsByDni(dni)) {
            throw new IllegalArgumentException("El DNI ya está en uso.");
        }
    }
    
    private void validarEmailUnico(String email) {
    	String regex = "^[a-zA-Z0-9._%+-]+@(gmail\\.com|hotmail\\.com|[a-zA-Z0-9.-]+\\.edu\\.pe)$";
        if (usuarioRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("El email ya está registrado.");
        }
        if (email == null || !email.matches(regex)) {
            throw new IllegalArgumentException("Coloque un email aceptable.");
        }
    }
    
    private void validarPassword(String password) {
        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException("La contraseña debe tener 8 o mas caracteres.");
        }

        if (!password.matches(".*[A-Z].*")) {
            throw new IllegalArgumentException("La contraseña debe contener al menos una letra mayúscula.");
        }

        if (!password.matches(".*[0-9].*")) {
            throw new IllegalArgumentException("La contraseña debe contener al menos un número.");
        }

        if (!password.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
            throw new IllegalArgumentException("La contraseña debe contener al menos un carácter especial.");
        }
    }

	public Usuario getUsuarioById(Long usuarioId) {
		return usuarioRepository.findById(usuarioId)
	            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con ID: " + usuarioId));
	}

}
