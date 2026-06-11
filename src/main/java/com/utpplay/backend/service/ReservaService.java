package com.utpplay.backend.service;

import com.utpplay.backend.dto.ReservaRequest;
import com.utpplay.backend.model.Reserva;
import com.utpplay.backend.model.Usuario;
import com.utpplay.backend.repository.ReservaRepository;
import com.utpplay.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

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

        Reserva reserva = new Reserva();
        reserva.setUsuario(usuarioOpt.get());
        reserva.setCancha(request.getCancha());
        reserva.setDeporte(request.getDeporte());
        reserva.setFecha(request.getFecha());
        reserva.setHorario(request.getHorario());
        reserva.setCapacidad(request.getCapacidad());
        reserva.setJugadoresActuales(1);
        reserva.setEstado("CONFIRMADA");

        return reservaRepository.save(reserva);
    }

    public boolean cancelarReserva(Long id, String studentId) {
        Optional<Reserva> reservaOpt = reservaRepository.findById(id);
        if (reservaOpt.isEmpty())
            return false;

        Reserva reserva = reservaOpt.get();
        if (!reserva.getUsuario().getStudentId().equals(studentId.toUpperCase()))
            return false;

        reserva.setEstado("CANCELADA");
        reservaRepository.save(reserva);
        return true;
    }
}