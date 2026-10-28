package com.guerrero.denunciaservice.service;

import com.guerrero.denunciaservice.entity.Denuncia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Contrato de la capa de negocio para el registro y atención de denuncias.
 */
public interface DenunciaService {

    /**
     * Registra una nueva denuncia. Asigna {@code estado = 1} (pendiente)
     * y la fecha/hora actual si no viene informada en el request.
     *
     * @param denuncia datos de la denuncia a registrar
     * @return la denuncia persistida con su ID generado
     */
    Denuncia registrarDenuncia(Denuncia denuncia);

    /**
     * Listado paginado de denuncias.
     *
     * @param pageable paginación (página y tamaño)
     * @return página de denuncias
     */
    Page<Denuncia> listarPaginado(Pageable pageable);

    /**
     * Obtiene una denuncia por su ID.
     *
     * @param id identificador de la denuncia
     * @return la denuncia encontrada
     * @throws com.guerrero.denunciaservice.exception.RecursoNoEncontradoException si no existe
     */
    Denuncia obtenerPorId(Long id);

    /**
     * Obtiene todas las denuncias registradas por el DNI de un ciudadano.
     *
     * @param dni DNI del ciudadano
     * @return lista de denuncias del ciudadano (posiblemente vacía)
     */
    List<Denuncia> obtenerPorDni(String dni);

    /**
     * Marca una denuncia como atendida (cambia {@code estado} de 1 a 0).
     *
     * @param id identificador de la denuncia
     * @return la denuncia actualizada
     */
    Denuncia atenderDenuncia(Long id);
}
