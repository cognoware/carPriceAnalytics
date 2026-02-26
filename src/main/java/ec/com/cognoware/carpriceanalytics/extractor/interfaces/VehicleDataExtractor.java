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

import ec.com.cognoware.carpriceanalytics.dto.ExtractedVehicleData;

import java.util.concurrent.CompletableFuture;

/**
 * Interfaz para extractores de datos vehiculares desde fuentes externas.
 *
 * Cada implementacion representa una fuente de datos (SRI, ANT, etc.)
 * con su propia prioridad para el merge de resultados.
 *
 * @author JJARA
 * @version 1.0.0
 * @since 2025-02-26
 */
public interface VehicleDataExtractor {

    /**
     * Extrae datos de un vehiculo por numero de placa.
     *
     * @param licensePlate numero de placa a consultar.
     * @return datos del vehiculo extraidos de forma asincrona.
     */
    CompletableFuture<ExtractedVehicleData> extractByPlate(
        String licensePlate
    );

    /**
     * Obtiene la prioridad del extractor para el merge.
     *
     * @return numero de prioridad (1 = mayor prioridad).
     */
    int getPriority();

    /**
     * Obtiene el nombre identificador de la fuente.
     *
     * @return nombre de la fuente de datos.
     */
    String getSourceName();
}
