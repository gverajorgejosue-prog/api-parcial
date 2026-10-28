package com.guerrero.denunciaservice.repository;

import com.guerrero.denunciaservice.entity.Denuncia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio JPA para la entidad {@link Denuncia}.
 * Hereda los métodos CRUD básicos de {@link JpaRepository} y agrega
 * consultas derivadas por DNI y utilidades de paginación.
 */
@Repository
public interface DenunciaRepository extends JpaRepository<Denuncia, Long> {

    /** Busca todas las denuncias presentadas por un ciudadano (por DNI). */
    List<Denuncia> findByDni(String dni);

    /** Versión paginada de la búsqueda por DNI. */
    Page<Denuncia> findByDni(String dni, Pageable pageable);

    /** Cuenta las denuncias según su estado (1 = pendientes, 0 = atendidas). */
    long countByEstado(Integer estado);
}
