package com.utpplay.backend.repository;

import com.utpplay.backend.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    List<Reserva> findByUsuario_StudentId(String studentId);

    List<Reserva> findByFecha(LocalDate fecha);

    List<Reserva> findByCanchaAndFechaAndHorario(String cancha, LocalDate fecha, String horario);

    boolean existsByCanchaAndFechaAndHorario(String cancha, LocalDate fecha, String horario);
}