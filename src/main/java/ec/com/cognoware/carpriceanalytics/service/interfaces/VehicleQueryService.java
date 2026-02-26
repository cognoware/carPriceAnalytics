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

import ec.com.cognoware.carpriceanalytics.dto.output.VehicleQueryOutput;

/**
 * Servicio principal de consulta de vehiculos.
 *
 * Orquesta la busqueda en cache, extraccion RPA y calculo de precios.
 *
 * @author JJARA
 * @version 1.0.0
 * @since 2025-02-26
 */
public interface VehicleQueryService {

    /**
     * Busca vehiculos por identificacion del propietario.
     *
     * @param identification cedula o RUC del propietario.
     * @return resultado con vehiculos y precios calculados.
     */
    VehicleQueryOutput searchByIdentification(
        String identification
    );

    /**
     * Busca vehiculo por numero de placa.
     *
     * @param licensePlate numero de placa del vehiculo.
     * @return resultado con vehiculo y precios calculados.
     */
    VehicleQueryOutput searchByPlate(String licensePlate);
}
