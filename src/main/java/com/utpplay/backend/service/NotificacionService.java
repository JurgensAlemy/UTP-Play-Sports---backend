package com.utpplay.backend.service;

import com.utpplay.backend.model.NotificacionVista;
import com.utpplay.backend.model.SolicitudMatchmaking;
import com.utpplay.backend.repository.NotificacionVistaRepository;
import com.utpplay.backend.repository.SolicitudMatchmakingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class NotificacionService {

    @Autowired
    private NotificacionVistaRepository notificacionVistaRepository;
    @Autowired
    private SolicitudMatchmakingRepository solicitudRepository;

    private List<SolicitudMatchmaking> activasParaOtros(String sid) {
        return solicitudRepository.findByEstadoOrderByCreadoEnDesc("ACTIVA")
                .stream()
                .filter(s -> !s.getUsuario().getStudentId().equalsIgnoreCase(sid))
                .toList();
    }

    @Transactional
    public List<SolicitudMatchmaking> getNuevas(String studentId) {
        String sid = studentId.toUpperCase();
        List<SolicitudMatchmaking> activas = activasParaOtros(sid);

        // Primera vez que este usuario consulta notificaciones (nunca tuvo registros):
        // marcamos todo lo actual como visto para no bombardearlo con publicaciones
        // viejas.
        if (!notificacionVistaRepository.existsByStudentId(sid)) {
            List<NotificacionVista> iniciales = activas.stream().map(s -> {
                NotificacionVista v = new NotificacionVista();
                v.setStudentId(sid);
                v.setSolicitud(s);
                return v;
            }).collect(Collectors.toList());
            if (!iniciales.isEmpty())
                notificacionVistaRepository.saveAll(iniciales);
            return List.of();
        }

        Set<Long> vistas = notificacionVistaRepository.findByStudentId(sid)
                .stream().map(v -> v.getSolicitud().getId()).collect(Collectors.toSet());

        return activas.stream().filter(s -> !vistas.contains(s.getId())).toList();
    }

    @Transactional
    public void marcarVistas(String studentId) {
        String sid = studentId.toUpperCase();
        Set<Long> yaVistas = notificacionVistaRepository.findByStudentId(sid)
                .stream().map(v -> v.getSolicitud().getId()).collect(Collectors.toSet());

        List<NotificacionVista> nuevas = activasParaOtros(sid).stream()
                .filter(s -> !yaVistas.contains(s.getId()))
                .map(s -> {
                    NotificacionVista v = new NotificacionVista();
                    v.setStudentId(sid);
                    v.setSolicitud(s);
                    return v;
                }).collect(Collectors.toList());

        if (!nuevas.isEmpty())
            notificacionVistaRepository.saveAll(nuevas);
    }
}