package com.utpplay.backend.controller;

import com.utpplay.backend.dto.ReservaRequest;
import com.utpplay.backend.service.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservas")
@CrossOrigin(origins = "http://localhost:5173")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @GetMapping("/usuario/{studentId}")
    public ResponseEntity<?> getReservasByUsuario(@PathVariable String studentId) {
        return ResponseEntity.ok(reservaService.getReservasByUsuario(studentId));
    }

    @GetMapping("/fecha/{fecha}")
    public ResponseEntity<?> getReservasByFecha(@PathVariable String fecha) {
        return ResponseEntity.ok(reservaService.getReservasByFecha(fecha));
    }

    @PostMapping
    public ResponseEntity<?> crearReserva(@RequestBody ReservaRequest request) {
        Object result = reservaService.crearReserva(request);
        if (result instanceof String) {
            return ResponseEntity.status(400).body(result);
        }
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}/usuario/{studentId}")
    public ResponseEntity<?> cancelarReserva(@PathVariable Long id, @PathVariable String studentId) {
        boolean ok = reservaService.cancelarReserva(id, studentId);
        return ok ? ResponseEntity.ok("Reserva cancelada.") : ResponseEntity.status(404).body("No encontrada.");
    }
}