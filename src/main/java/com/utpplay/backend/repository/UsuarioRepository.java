package com.utpplay.backend.repository;

import com.utpplay.backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByStudentId(String studentId);

    Optional<Usuario> findByEmail(String email);

    boolean existsByStudentId(String studentId);

    boolean existsByEmail(String email);

    List<Usuario> findByNombreContainingIgnoreCaseOrStudentIdContainingIgnoreCase(String nombre, String studentId);
}