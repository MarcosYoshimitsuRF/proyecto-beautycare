package com.beautycare.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción personalizada para errores de validación de negocio (ej. solapamiento de citas).
 * Cuando se lanza, Spring Boot devolverá una respuesta HTTP 400 (Bad Request).
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }
}