package com.utpplay.backend.repository;

import com.utpplay.backend.model.MensajeLeido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MensajeLeidoRepository extends JpaRepository<MensajeLeido, Long> {
    List<MensajeLeido> findByStudentId(String studentId);

    Optional<MensajeLeido> findByStudentIdAndConexion_Id(String studentId, Long conexionId);
}