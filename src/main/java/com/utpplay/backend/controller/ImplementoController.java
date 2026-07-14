package com.utpplay.backend.controller;

import com.utpplay.backend.service.ImplementoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/implementos")
@CrossOrigin(origins = "http://localhost:5173")
public class ImplementoController {

    @Autowired
    private ImplementoService implementoService;

    @GetMapping("/disponibilidad")
    public ResponseEntity<?> disponibilidad(
            @RequestParam String deporte,
            @RequestParam String fecha,
            @RequestParam String horario) {
        return ResponseEntity.ok(implementoService.getDisponibilidad(deporte, LocalDate.parse(fecha), horario));
    }

    @GetMapping("/prestamos/usuario/{studentId}")
    public ResponseEntity<?> prestamosActivos(@PathVariable String studentId) {
        return ResponseEntity.ok(implementoService.getPrestamosActivosDeUsuario(studentId));
    }
}