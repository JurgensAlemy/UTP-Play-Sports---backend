package com.utpplay.backend.service;

import com.utpplay.backend.dto.DisponibilidadImplementoDTO;
import com.utpplay.backend.dto.ImplementoSeleccionDTO;
import com.utpplay.backend.model.Implemento;
import com.utpplay.backend.model.Prestamo;
import com.utpplay.backend.model.Reserva;
import com.utpplay.backend.repository.ImplementoRepository;
import com.utpplay.backend.repository.PrestamoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ImplementoService {

    @Autowired
    private ImplementoRepository implementoRepository;
    @Autowired
    private PrestamoRepository prestamoRepository;

    private String deporteImplemento(String tipo, String deporteReserva) {
        boolean esGeneral = "CHALECO".equalsIgnoreCase(tipo)
                || "SILBATO".equalsIgnoreCase(tipo)
                || "BOTIQUIN".equalsIgnoreCase(tipo);
        return esGeneral ? "General" : deporteReserva;
    }

    public List<DisponibilidadImplementoDTO> getDisponibilidad(String deporte, LocalDate fecha, String horario) {
        List<Implemento> implementos = implementoRepository.findByDeporteIgnoreCaseOrDeporteIgnoreCase(deporte,
                "General");
        List<DisponibilidadImplementoDTO> resultado = new ArrayList<>();
        for (Implemento imp : implementos) {
            int prestado = prestamoRepository.sumPrestadoPorBloque(imp.getId(), fecha, horario);
            int disponible = Math.max(0, imp.getStockTotal() - prestado);
            resultado.add(new DisponibilidadImplementoDTO(imp.getId(), imp.getTipo(), imp.getDeporte(),
                    imp.getStockTotal(), disponible));
        }
        return resultado;
    }

    // Devuelve un mensaje de error si no hay stock, o null si todo bien
    public String validarDisponibilidad(String deporte, LocalDate fecha, String horario,
            List<ImplementoSeleccionDTO> selecciones) {
        for (ImplementoSeleccionDTO sel : selecciones) {
            if (sel.getCantidad() <= 0)
                continue;
            String deporteBuscar = deporteImplemento(sel.getTipo(), deporte);
            Optional<Implemento> impOpt = implementoRepository.findByTipoAndDeporteIgnoreCase(sel.getTipo(),
                    deporteBuscar);
            if (impOpt.isEmpty())
                return "No manejamos préstamo de " + sel.getTipo().toLowerCase() + " para " + deporte + ".";
            Implemento imp = impOpt.get();
            int prestado = prestamoRepository.sumPrestadoPorBloque(imp.getId(), fecha, horario);
            int disponible = imp.getStockTotal() - prestado;
            if (sel.getCantidad() > disponible) {
                String nombre = "PELOTA".equalsIgnoreCase(imp.getTipo()) ? "pelotas" : "chalecos";
                return "Solo quedan " + disponible + " " + nombre + " disponibles para ese horario.";
            }
        }
        return null;
    }

    @Transactional
    public void crearPrestamos(Reserva reserva, List<ImplementoSeleccionDTO> selecciones) {
        List<Prestamo> prestamos = new ArrayList<>();
        for (ImplementoSeleccionDTO sel : selecciones) {
            if (sel.getCantidad() <= 0)
                continue;
            String deporteBuscar = deporteImplemento(sel.getTipo(), reserva.getDeporte());
            Optional<Implemento> impOpt = implementoRepository.findByTipoAndDeporteIgnoreCase(sel.getTipo(),
                    deporteBuscar);
            if (impOpt.isEmpty())
                continue;
            Prestamo p = new Prestamo();
            p.setReserva(reserva);
            p.setImplemento(impOpt.get());
            p.setCantidad(sel.getCantidad());
            p.setEstado("PRESTADO");
            prestamos.add(p);
        }
        if (!prestamos.isEmpty())
            prestamoRepository.saveAll(prestamos);
    }

    @Transactional
    public void devolverPorReserva(Long reservaId) {
        List<Prestamo> prestamos = prestamoRepository.findByReserva_IdAndEstado(reservaId, "PRESTADO");
        for (Prestamo p : prestamos) {
            p.setEstado("DEVUELTO");
            p.setDevueltoEn(LocalDateTime.now());
        }
        if (!prestamos.isEmpty())
            prestamoRepository.saveAll(prestamos);
    }

    public List<Prestamo> getPrestamosActivosDeUsuario(String studentId) {
        return prestamoRepository.findByReserva_Usuario_StudentIdAndEstadoOrderByCreadoEnDesc(studentId.toUpperCase(),
                "PRESTADO");
    }
}