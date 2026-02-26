/**
 * carPriceAnalytics, Sistema de analisis de precios vehiculares
 * Copyright (c) 2025 Cognoware Cia. Ltda.
 * Todos los derechos reservados.
 * <p>
 * Este archivo forma parte del carPriceAnalytics de
 * Cognoware Cia. Ltda, desarrollado conforme a los requerimientos y
 * normativas establecidos.
 */
package ec.com.cognoware.carpriceanalytics.service.interfaces;

import ec.com.cognoware.carpriceanalytics.dto.ExtractedVehicleData;

import java.util.List;

/**
 * Servicio de extraccion de datos vehiculares desde fuentes externas.
 *
 * Orquesta llamadas paralelas a multiples proveedores habilitados.
 *
 * @author JJARA
 * @version 1.0.0
 * @since 2025-02-26
 */
public interface VehicleExtractionService {

    /**
     * Extrae datos de todas las fuentes por numero de placa.
     *
     * @param licensePlate numero de placa a consultar.
     * @return lista de datos ordenados por prioridad.
     */
    List<ExtractedVehicleData> extractAllByPlate(
        String licensePlate
    );
}
