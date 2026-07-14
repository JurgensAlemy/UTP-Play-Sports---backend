package com.utpplay.backend.repository;

import com.utpplay.backend.model.Implemento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ImplementoRepository extends JpaRepository<Implemento, Long> {
    Optional<Implemento> findByTipoAndDeporteIgnoreCase(String tipo, String deporte);

    List<Implemento> findByDeporteIgnoreCaseOrDeporteIgnoreCase(String deporte1, String deporte2);
}