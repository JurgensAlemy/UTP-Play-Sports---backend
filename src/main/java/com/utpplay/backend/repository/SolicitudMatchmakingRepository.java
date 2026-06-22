package com.utpplay.backend.repository;

import com.utpplay.backend.model.SolicitudMatchmaking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SolicitudMatchmakingRepository extends JpaRepository<SolicitudMatchmaking, Long> {
    List<SolicitudMatchmaking> findByEstadoOrderByCreadoEnDesc(String estado);

    List<SolicitudMatchmaking> findByUsuario_StudentId(String studentId);
}