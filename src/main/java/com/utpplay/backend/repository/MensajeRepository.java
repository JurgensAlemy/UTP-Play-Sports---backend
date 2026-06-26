package com.utpplay.backend.repository;

import com.utpplay.backend.model.Mensaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MensajeRepository extends JpaRepository<Mensaje, Long> {
    List<Mensaje> findByConexion_IdOrderByCreadoEnAsc(Long conexionId);
}