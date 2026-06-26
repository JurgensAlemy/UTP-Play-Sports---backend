package com.utpplay.backend.controller;

import com.utpplay.backend.dto.ReservaRequest;
import com.utpplay.backend.model.Reserva;
import com.utpplay.backend.service.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.utpplay.backend.repository.ReservaRepository;

@RestController
@RequestMapping("/api/reservas")
@CrossOrigin(origins = "http://localhost:5173")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private ReservaRepository reservaRepository;

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

    @DeleteMapping("/{reservaId}")
    public ResponseEntity<?> eliminarReserva(
            @PathVariable Long reservaId,
            @RequestParam String studentId) {
        try {
            Reserva reserva = reservaRepository.findById(reservaId).orElse(null);

            // 1. Validar si la reserva ya fue borrada o no existe
            if (reserva == null) {
                return ResponseEntity.status(404).body("La reserva ya fue eliminada del sistema.");
            }

            // 2. Validar que la reserva pertenezca al usuario (ignorando
            // mayúsculas/minúsculas)
            if (!reserva.getUsuario().getStudentId().equalsIgnoreCase(studentId.trim())) {
                return ResponseEntity.status(403).body("No tienes permiso para eliminar esta reserva");
            }

            reservaRepository.deleteById(reservaId);
            return ResponseEntity.ok("Reserva eliminada");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error interno del servidor al eliminar");
        }
    }

}