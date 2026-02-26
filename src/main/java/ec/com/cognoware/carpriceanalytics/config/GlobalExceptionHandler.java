/**
 * carPriceAnalytics, Sistema de analisis de precios vehiculares
 * Copyright (c) 2025 Cognoware Cia. Ltda.
 * Todos los derechos reservados.
 * <p>
 * Este archivo forma parte del carPriceAnalytics de
 * Cognoware Cia. Ltda, desarrollado conforme a los requerimientos y
 * normativas establecidos.
 */
package ec.com.cognoware.carpriceanalytics.config;

import ec.com.cognoware.carpriceanalytics.exception.ExtractionTimeoutException;
import ec.com.cognoware.carpriceanalytics.exception.VehicleNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Manejador global de excepciones para respuestas REST.
 *
 * Centraliza el manejo de errores y estandariza el formato
 * de respuesta de error en toda la API.
 *
 * @author JJARA
 * @version 1.0.0
 * @since 2025-02-26
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(
        GlobalExceptionHandler.class
    );

    /**
     * Maneja excepcion de vehiculo no encontrado.
     *
     * @param ex excepcion capturada.
     * @return respuesta 404 con detalle del error.
     */
    @ExceptionHandler(VehicleNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(
        VehicleNotFoundException ex
    ) {
        log.warn("Vehiculo no encontrado: {}", ex.getMessage());
        return buildErrorResponse(
            HttpStatus.NOT_FOUND,
            ex.getMessage()
        );
    }

    /**
     * Maneja excepcion de timeout en extraccion RPA.
     *
     * @param ex excepcion capturada.
     * @return respuesta 504 con detalle del error.
     */
    @ExceptionHandler(ExtractionTimeoutException.class)
    public ResponseEntity<Map<String, Object>> handleTimeout(
        ExtractionTimeoutException ex
    ) {
        log.error("Timeout en extraccion: {}", ex.getMessage());
        return buildErrorResponse(
            HttpStatus.GATEWAY_TIMEOUT,
            ex.getMessage()
        );
    }

    /**
     * Maneja excepciones generales no controladas.
     *
     * @param ex excepcion capturada.
     * @return respuesta 500 con detalle del error.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneral(
        Exception ex
    ) {
        log.error("Error inesperado: {}", ex.getMessage(), ex);
        return buildErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "Error interno del servidor"
        );
    }

    private ResponseEntity<Map<String, Object>> buildErrorResponse(
        HttpStatus status,
        String message
    ) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        return new ResponseEntity<>(body, status);
    }
}
