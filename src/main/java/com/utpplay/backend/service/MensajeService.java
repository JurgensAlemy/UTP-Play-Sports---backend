package com.utpplay.backend.service;

import com.utpplay.backend.model.Conexion;
import com.utpplay.backend.model.Mensaje;
import com.utpplay.backend.model.Usuario;
import com.utpplay.backend.repository.ConexionRepository;
import com.utpplay.backend.repository.MensajeRepository;
import com.utpplay.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MensajeService {

    @Autowired
    private MensajeRepository mensajeRepository;

    @Autowired
    private ConexionRepository conexionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Object enviarMensaje(Long conexionId, String studentId, String contenido, String imagenUrl) {
        Optional<Conexion> conexionOpt = conexionRepository.findById(conexionId);
        if (conexionOpt.isEmpty())
            return "Conexión no encontrada.";

        Conexion conexion = conexionOpt.get();

        // Solo los dos participantes de la conexión pueden chatear
        String idDuenio = conexion.getSolicitud().getUsuario().getStudentId();
        String idConector = conexion.getUsuario().getStudentId();
        if (!studentId.equalsIgnoreCase(idDuenio) && !studentId.equalsIgnoreCase(idConector)) {
            return "No tienes acceso a este chat.";
        }

        Optional<Usuario> remitenteOpt = usuarioRepository.findByStudentId(studentId.toUpperCase());
        if (remitenteOpt.isEmpty())
            return "Usuario no encontrado.";

        Mensaje msg = new Mensaje();
        msg.setConexion(conexion);
        msg.setRemitente(remitenteOpt.get());
        msg.setContenido(contenido);
        msg.setImagenUrl(imagenUrl);

        return mensajeRepository.save(msg);
    }

    public Object getMensajes(Long conexionId, String studentId) {
        Optional<Conexion> conexionOpt = conexionRepository.findById(conexionId);
        if (conexionOpt.isEmpty())
            return List.of();

        Conexion conexion = conexionOpt.get();
        String idDuenio = conexion.getSolicitud().getUsuario().getStudentId();
        String idConector = conexion.getUsuario().getStudentId();
        if (!studentId.equalsIgnoreCase(idDuenio) && !studentId.equalsIgnoreCase(idConector)) {
            return List.of();
        }

        return mensajeRepository.findByConexion_IdOrderByCreadoEnAsc(conexionId);
    }
}