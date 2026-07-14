package com.utpplay.backend.controller;

import com.utpplay.backend.service.MensajeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import java.util.Optional;
import java.util.List;
import java.util.Map;
import java.util.Arrays;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*", methods = {
        RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
        RequestMethod.DELETE, RequestMethod.OPTIONS
})
public class MensajeController {

    @Autowired
    private MensajeService mensajeService;

    @Autowired
    private com.utpplay.backend.repository.MensajeRepository mensajeRepository;

    private static final String UPLOAD_DIR = "uploads/";

    @GetMapping("/conexion/{conexionId}/usuario/{studentId}")
    public ResponseEntity<?> getMensajes(
            @PathVariable Long conexionId,
            @PathVariable String studentId) {
        return ResponseEntity.ok(mensajeService.getMensajes(conexionId, studentId));
    }

    @PostMapping("/conexion/{conexionId}/usuario/{studentId}")
    public ResponseEntity<?> enviarMensaje(
            @PathVariable Long conexionId,
            @PathVariable String studentId,
            @RequestParam(required = false) String contenido,
            @RequestParam(required = false) MultipartFile imagen) {

        String imagenUrl = null;

        if (imagen != null && !imagen.isEmpty()) {
            try {
                File dir = new File(UPLOAD_DIR);
                if (!dir.exists())
                    dir.mkdirs();

                String extension = StringUtils.getFilenameExtension(imagen.getOriginalFilename());
                String filename = UUID.randomUUID() + "." + extension;
                Path path = Paths.get(UPLOAD_DIR + filename);
                Files.write(path, imagen.getBytes());
                imagenUrl = "/uploads/" + filename;
            } catch (IOException e) {
                return ResponseEntity.status(500).body("Error al guardar imagen.");
            }
        }

        if ((contenido == null || contenido.isBlank()) && imagenUrl == null) {
            return ResponseEntity.status(400).body("El mensaje no puede estar vacío.");
        }

        Object result = mensajeService.enviarMensaje(conexionId, studentId, contenido, imagenUrl);
        if (result instanceof String) {
            return ResponseEntity.status(400).body(result);
        }
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{mensajeId}/usuario/{studentId}")
    public ResponseEntity<?> eliminarMensaje(
            @PathVariable Long mensajeId,
            @PathVariable String studentId) {
        Optional<com.utpplay.backend.model.Mensaje> opt = mensajeRepository.findById(mensajeId);
        if (opt.isEmpty())
            return ResponseEntity.status(404).body("Mensaje no encontrado.");
        com.utpplay.backend.model.Mensaje msg = opt.get();
        if (!msg.getRemitente().getStudentId().equalsIgnoreCase(studentId)) {
            return ResponseEntity.status(403).body("Sin permiso.");
        }
        mensajeRepository.deleteById(mensajeId);
        return ResponseEntity.ok("Eliminado.");
    }

    @GetMapping("/no-leidos/{studentId}")
    public ResponseEntity<?> getNoLeidos(
            @PathVariable String studentId,
            @RequestParam String conexionIds) { // ej: "1,4,9"
        List<Long> ids = Arrays.stream(conexionIds.split(","))
                .filter(s -> !s.isBlank())
                .map(Long::parseLong)
                .toList();
        Map<Long, Integer> resultado = mensajeService.getNoLeidosPorConexion(studentId, ids);
        return ResponseEntity.ok(resultado);
    }

    @PostMapping("/conexion/{conexionId}/usuario/{studentId}/marcar-leido")
    public ResponseEntity<?> marcarLeido(@PathVariable Long conexionId, @PathVariable String studentId) {
        mensajeService.marcarLeido(conexionId, studentId);
        return ResponseEntity.ok("Marcado como leído.");
    }
}