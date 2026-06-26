package com.utpplay.backend.controller;

import com.utpplay.backend.model.Usuario;
import com.utpplay.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Optional;

import com.utpplay.backend.dto.UsuarioBusquedaDTO;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.util.StringUtils;

import java.io.File;
import java.nio.file.Path;

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

    @GetMapping("/buscar")
    public ResponseEntity<?> buscarUsuarios(@RequestParam String q, @RequestParam(required = false) String excluir) {
        if (q == null || q.trim().length() < 2) {
            return ResponseEntity.ok(List.of());
        }
        List<Usuario> resultados = usuarioRepository
                .findByNombreContainingIgnoreCaseOrStudentIdContainingIgnoreCase(q.trim(), q.trim());

        List<UsuarioBusquedaDTO> dtos = resultados.stream()
                .filter(u -> excluir == null || !u.getStudentId().equalsIgnoreCase(excluir))
                .map(u -> new UsuarioBusquedaDTO(
                        u.getStudentId(), u.getNombre(), u.getFacultad(),
                        u.getDeporteFavorito(), u.getNivelHabilidad(), u.isVerificado()))
                .limit(20)
                .toList();

        return ResponseEntity.ok(dtos);
    }

    @PostMapping(value = "/{studentId}/foto", consumes = "multipart/form-data")
    @CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*", methods = {
            RequestMethod.POST, RequestMethod.OPTIONS
    })
    public ResponseEntity<?> subirFoto(
            @PathVariable String studentId,
            @RequestParam("foto") MultipartFile foto) {
        Optional<Usuario> opt = usuarioRepository.findByStudentId(studentId.toUpperCase());
        if (opt.isEmpty())
            return ResponseEntity.status(404).body("Usuario no encontrado.");

        try {
            File dir = new File("uploads/");
            if (!dir.exists())
                dir.mkdirs();
            String extension = StringUtils.getFilenameExtension(foto.getOriginalFilename());
            String filename = "avatar_" + studentId.toUpperCase() + "." + extension;
            Path path = Paths.get("uploads/" + filename);
            Files.write(path, foto.getBytes());

            Usuario u = opt.get();
            u.setFotoPerfil("/uploads/" + filename);
            usuarioRepository.save(u);

            // Devolver solo los datos necesarios, no el objeto completo con password
            return ResponseEntity.ok(java.util.Map.of(
                    "studentId", u.getStudentId(),
                    "nombre", u.getNombre(),
                    "fotoPerfil", u.getFotoPerfil()));
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error al guardar foto.");
        }
    }
}