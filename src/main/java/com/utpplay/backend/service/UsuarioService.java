package com.utpplay.backend.service;

import com.utpplay.backend.dto.LoginRequest;
import com.utpplay.backend.dto.LoginResponse;
import com.utpplay.backend.dto.RegisterRequest;
import com.utpplay.backend.model.Usuario;
import com.utpplay.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public LoginResponse login(LoginRequest request) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByStudentId(
                request.getStudentId().toUpperCase());

        if (usuarioOpt.isEmpty()) {
            return new LoginResponse(false, "Usuario no encontrado.");
        }

        Usuario usuario = usuarioOpt.get();

        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            return new LoginResponse(false, "Contraseña incorrecta.");
        }

        return new LoginResponse(
                true,
                "Login exitoso.",
                usuario.getStudentId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getFacultad(),
                usuario.isVerificado());
    }

    public LoginResponse register(RegisterRequest request) {
        String studentId = request.getStudentId().toUpperCase();

        if (!studentId.matches("^U\\d{7,9}$")) {
            return new LoginResponse(false, "ID inválido. Formato: U seguido de 7 a 9 dígitos.");
        }

        if (usuarioRepository.existsByStudentId(studentId)) {
            return new LoginResponse(false, "Este ID de estudiante ya está registrado.");
        }

        if (usuarioRepository.existsByEmail(request.getEmail())) {
            return new LoginResponse(false, "Este correo ya está registrado.");
        }

        Usuario usuario = new Usuario();
        usuario.setStudentId(studentId);
        usuario.setNombre(request.getNombre());
        usuario.setEmail(request.getEmail());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setFacultad(request.getFacultad());
        usuario.setVerificado(true);

        usuarioRepository.save(usuario);

        return new LoginResponse(
                true,
                "Registro exitoso.",
                usuario.getStudentId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getFacultad(),
                usuario.isVerificado());
    }
}