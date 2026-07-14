package com.utpplay.backend.repository;

import com.utpplay.backend.model.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {

    List<Prestamo> findByReserva_IdAndEstado(Long reservaId, String estado);

    // Suma cuánto de un implemento ya está prestado en ese mismo bloque de
    // fecha+horario
    @Query("SELECT COALESCE(SUM(p.cantidad),0) FROM Prestamo p " +
            "WHERE p.implemento.id = :implementoId AND p.reserva.fecha = :fecha " +
            "AND p.reserva.horario = :horario AND p.estado = 'PRESTADO'")
    int sumPrestadoPorBloque(@Param("implementoId") Long implementoId,
            @Param("fecha") LocalDate fecha,
            @Param("horario") String horario);

    List<Prestamo> findByReserva_Usuario_StudentIdAndEstadoOrderByCreadoEnDesc(String studentId, String estado);
}