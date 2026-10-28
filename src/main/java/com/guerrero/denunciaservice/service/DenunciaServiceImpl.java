package com.guerrero.denunciaservice.service;

import com.guerrero.denunciaservice.entity.Denuncia;
import com.guerrero.denunciaservice.exception.RecursoNoEncontradoException;
import com.guerrero.denunciaservice.repository.DenunciaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementación de la lógica de negocio de {@link DenunciaService}.
 */
@Service
public class DenunciaServiceImpl implements DenunciaService {

    private static final Logger log = LoggerFactory.getLogger(DenunciaServiceImpl.class);

    private final DenunciaRepository denunciaRepository;

    public DenunciaServiceImpl(DenunciaRepository denunciaRepository) {
        this.denunciaRepository = denunciaRepository;
    }

    @Override
    @Transactional
    public Denuncia registrarDenuncia(Denuncia denuncia) {
        // La fecha llega por defecto la actual si el cliente no la envía
        if (denuncia.getFecha() == null) {
            denuncia.setFecha(LocalDateTime.now());
        }
        // Toda denuncia nueva nace pendiente de atención
        denuncia.setEstado(Denuncia.ESTADO_PENDIENTE);

        Denuncia guardada = denunciaRepository.save(denuncia);
        log.info("Denuncia registrada con id {} para el DNI {}", guardada.getId(), guardada.getDni());
        return guardada;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Denuncia> listarPaginado(Pageable pageable) {
        return denunciaRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Denuncia obtenerPorId(Long id) {
        return denunciaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la denuncia con id " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Denuncia> obtenerPorDni(String dni) {
        return denunciaRepository.findByDni(dni);
    }

    @Override
    @Transactional
    public Denuncia atenderDenuncia(Long id) {
        Denuncia denuncia = obtenerPorId(id);
        denuncia.setEstado(Denuncia.ESTADO_ATENDIDA);
        Denuncia actualizada = denunciaRepository.save(denuncia);
        log.info("Denuncia {} marcada como atendida", id);
        return actualizada;
    }
}
