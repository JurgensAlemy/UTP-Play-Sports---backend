package com.utpplay.backend.controller;

import com.utpplay.backend.dto.ConexionRequest;
import com.utpplay.backend.dto.RespuestaConexionRequest;
import com.utpplay.backend.dto.SolicitudRequest;
import com.utpplay.backend.service.MatchmakingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/matchmaking")
@CrossOrigin(origins = "http://localhost:5173")
public class MatchmakingController {

    @Autowired
    private MatchmakingService matchmakingService;

    @GetMapping("/solicitudes")
    public ResponseEntity<?> getSolicitudesActivas() {
        return ResponseEntity.ok(matchmakingService.getSolicitudesActivas());
    }

    @GetMapping("/solicitudes/usuario/{studentId}")
    public ResponseEntity<?> getSolicitudesByUsuario(@PathVariable String studentId) {
        return ResponseEntity.ok(matchmakingService.getSolicitudesByUsuario(studentId));
    }

    @PostMapping("/solicitudes")
    public ResponseEntity<?> crearSolicitud(@RequestBody SolicitudRequest request) {
        Object result = matchmakingService.crearSolicitud(request);
        if (result instanceof String) {
            return ResponseEntity.status(400).body(result);
        }
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/solicitudes/{id}/usuario/{studentId}")
    public ResponseEntity<?> cerrarSolicitud(@PathVariable Long id, @PathVariable String studentId) {
        boolean ok = matchmakingService.cerrarSolicitud(id, studentId);
        return ok ? ResponseEntity.ok("Solicitud cerrada.") : ResponseEntity.status(404).body("No encontrada.");
    }

    @PostMapping("/conectar")
    public ResponseEntity<?> conectar(@RequestBody ConexionRequest request) {
        Object result = matchmakingService.conectar(request);
        if (result instanceof String) {
            return ResponseEntity.status(400).body(result);
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/conexiones/usuario/{studentId}")
    public ResponseEntity<?> getConexiones(@PathVariable String studentId) {
        return ResponseEntity.ok(matchmakingService.getConexionesByUsuario(studentId));
    }

    @GetMapping("/conexiones/recibidas/{studentId}")
    public ResponseEntity<?> getSolicitudesRecibidas(@PathVariable String studentId) {
        return ResponseEntity.ok(matchmakingService.getSolicitudesRecibidas(studentId));
    }

    @PutMapping("/conexiones/{conexionId}/responder")
    public ResponseEntity<?> responderConexion(@PathVariable Long conexionId,
            @RequestBody RespuestaConexionRequest request) {
        Object result = matchmakingService.responderConexion(conexionId, request);
        if (result instanceof String) {
            return ResponseEntity.status(400).body(result);
        }
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/conexiones/{conexionId}/usuario/{studentId}")
    public ResponseEntity<?> eliminarConexion(@PathVariable Long conexionId, @PathVariable String studentId) {
        Object result = matchmakingService.eliminarConexion(conexionId, studentId);
        if (result instanceof String && ((String) result).contains("no encontrad")) {
            return ResponseEntity.status(404).body(result);
        }
        if (result instanceof String && ((String) result).contains("permiso")) {
            return ResponseEntity.status(403).body(result);
        }
        return ResponseEntity.ok(result);
    }
}