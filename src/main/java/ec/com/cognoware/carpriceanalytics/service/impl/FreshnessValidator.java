/**
 * carPriceAnalytics, Sistema de analisis de precios vehiculares
 * Copyright (c) 2025 Cognoware Cia. Ltda.
 * Todos los derechos reservados.
 * <p>
 * Este archivo forma parte del carPriceAnalytics de
 * Cognoware Cia. Ltda, desarrollado conforme a los requerimientos y
 * normativas establecidos.
 */
package ec.com.cognoware.carpriceanalytics.service.impl;

import ec.com.cognoware.carpriceanalytics.model.Vehicle;
import ec.com.cognoware.carpriceanalytics.util.AppConstants;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Validador de frescura de datos en cache.
 *
 * Determina si los datos de un vehiculo necesitan ser
 * actualizados desde las fuentes externas.
 *
 * @author JJARA
 * @version 1.0.0
 * @since 2025-02-26
 */
@Component
public class FreshnessValidator {

    /**
     * Valida si un vehiculo requiere actualizacion.
     *
     * @param vehicle vehiculo a validar.
     * @param limitDays limite de dias de vigencia.
     * @return true si los datos estan expirados.
     */
    public boolean isExpired(Vehicle vehicle, int limitDays) {
        if (vehicle == null
            || vehicle.getLastUpdateDate() == null) {
            return true;
        }
        LocalDateTime expirationDate =
            vehicle.getLastUpdateDate().plusDays(limitDays);
        return LocalDateTime.now().isAfter(expirationDate);
    }

    /**
     * Filtra vehiculos expirados de una lista.
     *
     * @param vehicles lista de vehiculos a validar.
     * @param limitDays limite de dias de vigencia.
     * @return lista de vehiculos que requieren actualizacion.
     */
    public List<Vehicle> findExpired(
        List<Vehicle> vehicles,
        int limitDays
    ) {
        return vehicles.stream()
            .filter(v -> isExpired(v, limitDays))
            .toList();
    }

    /**
     * Valida si algun vehiculo de la lista esta expirado.
     *
     * @param vehicles lista de vehiculos a validar.
     * @param limitDays limite de dias de vigencia.
     * @return true si al menos uno esta expirado.
     */
    public boolean hasExpired(
        List<Vehicle> vehicles,
        int limitDays
    ) {
        return vehicles.stream()
            .anyMatch(v -> isExpired(v, limitDays));
    }
}
