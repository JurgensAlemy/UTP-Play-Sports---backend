package com.utpplay.backend.service;

import com.utpplay.backend.dto.ReservaRequest;
import com.utpplay.backend.model.Reserva;
import com.utpplay.backend.model.Usuario;
import com.utpplay.backend.repository.ReservaRepository;
import com.utpplay.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.utpplay.backend.dto.ImplementoSeleccionDTO;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ImplementoService implementoService;

    public List<Reserva> getReservasByUsuario(String studentId) {
        return reservaRepository.findByUsuario_StudentId(studentId.toUpperCase());
    }

    public List<Reserva> getReservasByFecha(String fecha) {
        return reservaRepository.findByFecha(java.time.LocalDate.parse(fecha));
    }

    public Object crearReserva(ReservaRequest request) {
        String studentId = request.getStudentId().toUpperCase();

        Optional<Usuario> usuarioOpt = usuarioRepository.findByStudentId(studentId);
        if (usuarioOpt.isEmpty()) {
            return "Usuario no encontrado.";
        }

        boolean ocupada = reservaRepository.existsByCanchaAndFechaAndHorario(
                request.getCancha(), request.getFecha(), request.getHorario());
        if (ocupada) {
            return "La cancha ya está reservada en ese horario.";
        }

        // Validar stock de implementos ANTES de crear la reserva
        if (request.getImplementos() != null && !request.getImplementos().isEmpty()) {
            String errorStock = implementoService.validarDisponibilidad(
                    request.getDeporte(), request.getFecha(), request.getHorario(), request.getImplementos());
            if (errorStock != null)
                return errorStock;
        }

        Reserva reserva = new Reserva();
        reserva.setUsuario(usuarioOpt.get());
        reserva.setCancha(request.getCancha());
        reserva.setDeporte(request.getDeporte());
        reserva.setFecha(request.getFecha());
        reserva.setHorario(request.getHorario());
        reserva.setCapacidad(request.getCapacidad());
        reserva.setJugadoresActuales(1);
        reserva.setEstado("CONFIRMADA");
        reserva = reservaRepository.save(reserva);

        if (request.getImplementos() != null && !request.getImplementos().isEmpty()) {
            implementoService.crearPrestamos(reserva, request.getImplementos());
        }

        return reserva;
    }

    public boolean cancelarReserva(Long id, String studentId) {
        Reserva reserva = reservaRepository.findById(id).orElse(null);
        if (reserva == null || !reserva.getUsuario().getStudentId().equals(studentId)) {
            return false;
        }
        reserva.setEstado("CANCELADA");
        reserva.setCanceladoEn(LocalDateTime.now());
        reservaRepository.save(reserva);
        implementoService.devolverPorReserva(id); // libera pelotas/chalecos de inmediato
        return true;
    }
}