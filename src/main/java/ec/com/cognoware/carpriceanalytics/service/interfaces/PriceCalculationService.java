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

import ec.com.cognoware.carpriceanalytics.model.Vehicle;

/**
 * Servicio de calculo de precios vehiculares.
 *
 * Orquesta fuentes de precios y aplica homologacion de modelos.
 *
 * @author JJARA
 * @version 1.0.0
 * @since 2025-02-26
 */
public interface PriceCalculationService {

    /**
     * Calcula y asigna precios a un vehiculo.
     *
     * @param vehicle vehiculo al que se calcularan precios.
     * @return vehiculo con precios asignados.
     */
    Vehicle calculateAndApplyPrices(Vehicle vehicle);
}
