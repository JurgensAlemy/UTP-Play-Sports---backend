package com.utpplay.backend.controller;

import com.utpplay.backend.service.NotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notificaciones")
@CrossOrigin(origins = "http://localhost:5173")
public class NotificacionController {

    @Autowired
    private NotificacionService notificacionService;

    @GetMapping("/nuevas/{studentId}")
    public ResponseEntity<?> getNuevas(@PathVariable String studentId) {
        return ResponseEntity.ok(notificacionService.getNuevas(studentId));
    }

    @PostMapping("/marcar-vistas/{studentId}")
    public ResponseEntity<?> marcarVistas(@PathVariable String studentId) {
        notificacionService.marcarVistas(studentId);
        return ResponseEntity.ok("Marcadas como vistas.");
    }
}