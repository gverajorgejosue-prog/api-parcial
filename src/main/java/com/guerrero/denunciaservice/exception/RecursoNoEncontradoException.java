package com.guerrero.denunciaservice.exception;

/**
 * Excepción de negocio lanzada cuando un recurso no existe en la base de datos.
 * Es capturada por {@link GlobalExceptionHandler} para responder HTTP 404.
 */
public class RecursoNoEncontradoException extends RuntimeException {

    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }

    public RecursoNoEncontradoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
