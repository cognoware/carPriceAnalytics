/**
 * carPriceAnalytics, Sistema de analisis de precios vehiculares
 * Copyright (c) 2025 Cognoware Cia. Ltda.
 * Todos los derechos reservados.
 * <p>
 * Este archivo forma parte del carPriceAnalytics de
 * Cognoware Cia. Ltda, desarrollado conforme a los requerimientos y
 * normativas establecidos.
 */
package ec.com.cognoware.carpriceanalytics.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Controlador de salud del servicio.
 *
 * Endpoint obligatorio segun estandar de desarrollo.
 *
 * @author JJARA
 * @version 1.0.0
 * @since 2025-02-26
 */
@RestController
@Tag(name = "Health", description = "Estado del servicio")
public class HealthController {

    /**
     * Retorna el estado de salud del servicio.
     *
     * @return estado actual del servicio.
     */
    @Operation(
        summary = "Health check",
        description = "Verifica que el servicio este activo"
    )
    @GetMapping(value = "/health", produces = "application/json")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "UP");
        response.put("service", "carPriceAnalytics");
        response.put("timestamp", LocalDateTime.now().toString());
        return ResponseEntity.ok(response);
    }
}
