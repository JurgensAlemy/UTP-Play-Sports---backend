package com.utpplay.backend.repository;

import com.utpplay.backend.model.NotificacionVista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotificacionVistaRepository extends JpaRepository<NotificacionVista, Long> {
    List<NotificacionVista> findByStudentId(String studentId);

    boolean existsByStudentId(String studentId);
}