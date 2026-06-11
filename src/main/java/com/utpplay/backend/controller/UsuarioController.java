package com.utpplay.backend.controller;

import com.utpplay.backend.model.Usuario;
import com.utpplay.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "http://localhost:5173")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/{studentId}")
    public ResponseEntity<?> getPerfil(@PathVariable String studentId) {
        Optional<Usuario> usuario = usuarioRepository.findByStudentId(studentId.toUpperCase());
        if (usuario.isEmpty()) {
            return ResponseEntity.status(404).body("Usuario no encontrado.");
        }
        return ResponseEntity.ok(usuario.get());
    }

    @PutMapping("/{studentId}")
    public ResponseEntity<?> actualizarPerfil(
            @PathVariable String studentId,
            @RequestBody Usuario datos) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByStudentId(studentId.toUpperCase());
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Usuario no encontrado.");
        }
        Usuario usuario = usuarioOpt.get();
        if (datos.getNombre() != null)
            usuario.setNombre(datos.getNombre());
        if (datos.getFacultad() != null)
            usuario.setFacultad(datos.getFacultad());
        if (datos.getDeporteFavorito() != null)
            usuario.setDeporteFavorito(datos.getDeporteFavorito());
        if (datos.getNivelHabilidad() != null)
            usuario.setNivelHabilidad(datos.getNivelHabilidad());
        usuarioRepository.save(usuario);
        return ResponseEntity.ok(usuario);
    }
}