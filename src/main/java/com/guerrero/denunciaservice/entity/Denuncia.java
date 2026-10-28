package com.guerrero.denunciaservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Entidad que representa una denuncia ciudadana registrada en el sistema.
 * Se mapea a la tabla {@code denuncias}.
 */
@Entity
@Table(name = "denuncias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Denuncia {

    /** Estado inicial: denuncia registrada, pendiente de atención. */
    public static final int ESTADO_PENDIENTE = 1;

    /** Estado final: denuncia atendida por la autoridad competente. */
    public static final int ESTADO_ATENDIDA = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** DNI del ciudadano que presenta la denuncia (8 dígitos). */
    @Column(name = "dni", length = 8, nullable = false)
    private String dni;

    /** Fecha y hora de registro de la denuncia. */
    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    /** Tipo de delito: ROBO, ASALTO, VANDALISMO, etc. */
    @Column(name = "tipo", length = 30, nullable = false)
    private String tipo;

    /** Lugar donde ocurrieron los hechos. */
    @Column(name = "direccion", length = 200, nullable = false)
    private String direccion;

    /** Relato de los hechos. */
    @Column(name = "descripcion", length = 255, nullable = false)
    private String descripcion;

    /** 1 = Pendiente/Registrada, 0 = Atendida. */
    @Column(name = "estado", nullable = false, columnDefinition = "INTEGER DEFAULT 1")
    @Builder.Default
    private Integer estado = ESTADO_PENDIENTE;
}
