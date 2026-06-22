package com.utpplay.backend.service;

import com.utpplay.backend.dto.ConexionRequest;
import com.utpplay.backend.dto.RespuestaConexionRequest;
import com.utpplay.backend.dto.SolicitudRequest;
import com.utpplay.backend.model.Conexion;
import com.utpplay.backend.model.SolicitudMatchmaking;
import com.utpplay.backend.model.Usuario;
import com.utpplay.backend.repository.ConexionRepository;
import com.utpplay.backend.repository.SolicitudMatchmakingRepository;
import com.utpplay.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class MatchmakingService {

    @Autowired
    private SolicitudMatchmakingRepository solicitudRepository;

    @Autowired
    private ConexionRepository conexionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<SolicitudMatchmaking> getSolicitudesActivas() {
        return solicitudRepository.findByEstadoOrderByCreadoEnDesc("ACTIVA");
    }

    public List<SolicitudMatchmaking> getSolicitudesByUsuario(String studentId) {
        return solicitudRepository.findByUsuario_StudentId(studentId.toUpperCase());
    }

    public Object crearSolicitud(SolicitudRequest request) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByStudentId(request.getStudentId().toUpperCase());
        if (usuarioOpt.isEmpty()) {
            return "Usuario no encontrado.";
        }

        SolicitudMatchmaking solicitud = new SolicitudMatchmaking();
        solicitud.setUsuario(usuarioOpt.get());
        solicitud.setDeporte(request.getDeporte());
        solicitud.setNivel(request.getNivel());
        solicitud.setDisponibilidad(request.getDisponibilidad());
        solicitud.setDescripcion(request.getDescripcion());
        solicitud.setEstado("ACTIVA");

        return solicitudRepository.save(solicitud);
    }

    // Crea una conexión en estado PENDIENTE (ya no toggle, ahora es una solicitud
    // real)
    public Object conectar(ConexionRequest request) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByStudentId(request.getStudentId().toUpperCase());
        if (usuarioOpt.isEmpty()) {
            return "Usuario no encontrado.";
        }

        Optional<SolicitudMatchmaking> solicitudOpt = solicitudRepository.findById(request.getSolicitudId());
        if (solicitudOpt.isEmpty()) {
            return "Solicitud no encontrada.";
        }

        if (solicitudOpt.get().getUsuario().getStudentId().equals(request.getStudentId().toUpperCase())) {
            return "No puedes conectarte a tu propia solicitud.";
        }

        Optional<Conexion> existente = conexionRepository.findBySolicitud_IdAndUsuario_StudentId(
                request.getSolicitudId(), request.getStudentId().toUpperCase());
        if (existente.isPresent()) {
            return "Ya enviaste una solicitud de conexión aquí.";
        }

        Conexion conexion = new Conexion();
        conexion.setSolicitud(solicitudOpt.get());
        conexion.setUsuario(usuarioOpt.get());
        conexion.setMensaje(request.getMensaje());
        conexion.setEstado("PENDIENTE");

        return conexionRepository.save(conexion);
    }

    // Conexiones que YO envié (para ver el estado: pendiente/aceptada/rechazada)
    public List<Conexion> getConexionesByUsuario(String studentId) {
        return conexionRepository.findByUsuario_StudentId(studentId.toUpperCase());
    }

    // Solicitudes recibidas: gente que quiere unirse a MIS publicaciones
    public List<Conexion> getSolicitudesRecibidas(String studentId) {
        return conexionRepository.findBySolicitud_Usuario_StudentIdOrderByCreadoEnDesc(studentId.toUpperCase());
    }

    // El dueño de la solicitud acepta o rechaza
    public Object responderConexion(Long conexionId, RespuestaConexionRequest request) {
        Optional<Conexion> conexionOpt = conexionRepository.findById(conexionId);
        if (conexionOpt.isEmpty()) {
            return "Conexión no encontrada.";
        }

        Conexion conexion = conexionOpt.get();

        if (!conexion.getSolicitud().getUsuario().getStudentId().equals(request.getStudentId().toUpperCase())) {
            return "No tienes permiso para responder esta solicitud.";
        }

        if (!request.getEstado().equals("ACEPTADA") && !request.getEstado().equals("RECHAZADA")) {
            return "Estado inválido.";
        }

        conexion.setEstado(request.getEstado());
        return conexionRepository.save(conexion);
    }

    public boolean cerrarSolicitud(Long id, String studentId) {
        Optional<SolicitudMatchmaking> solicitudOpt = solicitudRepository.findById(id);
        if (solicitudOpt.isEmpty())
            return false;

        SolicitudMatchmaking solicitud = solicitudOpt.get();
        if (!solicitud.getUsuario().getStudentId().equals(studentId.toUpperCase()))
            return false;

        solicitud.setEstado("CERRADA");
        solicitudRepository.save(solicitud);
        return true;
    }
}