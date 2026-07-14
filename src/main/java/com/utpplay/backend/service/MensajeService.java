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

import com.utpplay.backend.model.MensajeLeido;
import com.utpplay.backend.repository.MensajeLeidoRepository;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
public class MensajeService {

    @Autowired
    private MensajeRepository mensajeRepository;

    @Autowired
    private ConexionRepository conexionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private MensajeLeidoRepository mensajeLeidoRepository;

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

    // Devuelve un mapa { conexionId: cantidadNoLeidos } para TODAS las conexiones
    // del usuario,
    // calculado 100% desde la BD (sirve para el badge de "Chat" en el nav)
    public Map<Long, Integer> getNoLeidosPorConexion(String studentId, List<Long> conexionIds) {
        String sid = studentId.toUpperCase();
        Map<Long, Long> ultimoVisto = mensajeLeidoRepository.findByStudentId(sid).stream()
                .collect(Collectors.toMap(v -> v.getConexion().getId(), MensajeLeido::getUltimoMensajeVistoId));

        Map<Long, Integer> resultado = new HashMap<>();
        for (Long conexionId : conexionIds) {
            Long visto = ultimoVisto.getOrDefault(conexionId, 0L);
            long noLeidos = mensajeRepository.findByConexion_IdOrderByCreadoEnAsc(conexionId).stream()
                    .filter(m -> m.getId() > visto && !m.getRemitente().getStudentId().equalsIgnoreCase(sid))
                    .count();
            resultado.put(conexionId, (int) noLeidos);
        }
        return resultado;
    }

    // Marca como leído hasta el mensaje más reciente de esa conexión
    public void marcarLeido(Long conexionId, String studentId) {
        String sid = studentId.toUpperCase();
        List<com.utpplay.backend.model.Mensaje> mensajes = mensajeRepository
                .findByConexion_IdOrderByCreadoEnAsc(conexionId);
        if (mensajes.isEmpty())
            return;
        Long ultimoId = mensajes.get(mensajes.size() - 1).getId();

        MensajeLeido registro = mensajeLeidoRepository.findByStudentIdAndConexion_Id(sid, conexionId)
                .orElseGet(() -> {
                    MensajeLeido nuevo = new MensajeLeido();
                    nuevo.setStudentId(sid);
                    nuevo.setConexion(mensajes.get(0).getConexion());
                    return nuevo;
                });
        registro.setUltimoMensajeVistoId(ultimoId);
        registro.setActualizadoEn(java.time.LocalDateTime.now());
        mensajeLeidoRepository.save(registro);
    }
}