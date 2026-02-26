/**
 * carPriceAnalytics, Sistema de analisis de precios vehiculares
 * Copyright (c) 2025 Cognoware Cia. Ltda.
 * Todos los derechos reservados.
 * <p>
 * Este archivo forma parte del carPriceAnalytics de
 * Cognoware Cia. Ltda, desarrollado conforme a los requerimientos y
 * normativas establecidos.
 */
package ec.com.cognoware.carpriceanalytics.extractor.interfaces;

import ec.com.cognoware.carpriceanalytics.dto.output.PriceDetailOutput;
import ec.com.cognoware.carpriceanalytics.model.Vehicle;

import java.util.concurrent.CompletableFuture;

/**
 * Interfaz para extractores de precios vehiculares.
 *
 * Cada implementacion consulta una fuente de precios diferente
 * (PatioTuerca, Generali, etc.)
 *
 * @author JJARA
 * @version 1.0.0
 * @since 2025-02-26
 */
public interface VehiclePriceExtractor {

    /**
     * Extrae precios para un vehiculo dado.
     *
     * @param vehicle entidad con los datos del vehiculo.
     * @return detalle de precios de forma asincrona.
     */
    CompletableFuture<PriceDetailOutput> extractPrice(
        Vehicle vehicle
    );

    /**
     * Obtiene el nombre de la fuente de precios.
     *
     * @return nombre de la fuente.
     */
    String getSourceName();
}
