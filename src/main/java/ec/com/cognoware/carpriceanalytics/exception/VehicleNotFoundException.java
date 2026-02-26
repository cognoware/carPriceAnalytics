/**
 * carPriceAnalytics, Sistema de analisis de precios vehiculares
 * Copyright (c) 2025 Cognoware Cia. Ltda.
 * Todos los derechos reservados.
 * <p>
 * Este archivo forma parte del carPriceAnalytics de
 * Cognoware Cia. Ltda, desarrollado conforme a los requerimientos y
 * normativas establecidos.
 */
package ec.com.cognoware.carpriceanalytics.exception;

import java.io.Serial;

/**
 * Excepcion lanzada cuando no se encuentra un vehiculo.
 *
 * @author JJARA
 * @version 1.0.0
 * @since 2025-02-26
 */
public class VehicleNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Crea excepcion con mensaje descriptivo.
     *
     * @param message detalle del vehiculo no encontrado.
     */
    public VehicleNotFoundException(String message) {
        super(message);
    }
}
