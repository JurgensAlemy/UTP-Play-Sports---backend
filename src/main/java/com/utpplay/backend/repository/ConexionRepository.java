package com.utpplay.backend.repository;

import com.utpplay.backend.model.Conexion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConexionRepository extends JpaRepository<Conexion, Long> {
    List<Conexion> findByUsuario_StudentId(String studentId);

    Optional<Conexion> findBySolicitud_IdAndUsuario_StudentId(Long solicitudId, String studentId);

    long countBySolicitud_Id(Long solicitudId);

    // Solicitudes recibidas: conexiones donde YO soy el dueño de la solicitud (no
    // el que pidió unirse)
    List<Conexion> findBySolicitud_Usuario_StudentIdOrderByCreadoEnDesc(String studentId);
}