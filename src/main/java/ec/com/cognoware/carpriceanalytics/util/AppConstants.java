/**
 * carPriceAnalytics, Sistema de analisis de precios vehiculares
 * Copyright (c) 2025 Cognoware Cia. Ltda.
 * Todos los derechos reservados.
 * <p>
 * Este archivo forma parte del carPriceAnalytics de
 * Cognoware Cia. Ltda, desarrollado conforme a los requerimientos y
 * normativas establecidos.
 */
package ec.com.cognoware.carpriceanalytics.util;

/**
 * Constantes generales de la aplicacion.
 *
 * @author JJARA
 * @version 1.0.0
 * @since 2025-02-26
 */
public final class AppConstants {

    private AppConstants() {
    }

    /**
     * Prioridad 1: proveedor primario (mayor confiabilidad).
     */
    public static final int ORDER_PRIORITY_ONE = 1;

    /**
     * Prioridad 2: proveedor secundario.
     */
    public static final int ORDER_PRIORITY_TWO = 2;

    /**
     * Prioridad 3: proveedor terciario alternativo.
     */
    public static final int ORDER_PRIORITY_THREE = 3;

    /**
     * Prioridad 4: proveedor terciario.
     */
    public static final int ORDER_PRIORITY_FOUR = 4;

    /**
     * Timeout por defecto para conexion en milisegundos
     */
    public static final int DEFAULT_CONNECT_TIMEOUT_MS = 3000;

    /**
     * Timeout por defecto para lectura en milisegundos
     */
    public static final int DEFAULT_READ_TIMEOUT_MS = 30000;

    /**
     * Timeout por defecto de extraccion en milisegundos
     */
    public static final int DEFAULT_EXTRACTION_TIMEOUT_MS = 25000;

    /**
     * Timeout para calculo de precios en milisegundos
     */
    public static final int DEFAULT_PRICE_TIMEOUT_MS = 20000;

    /**
     * Limite de dias por defecto para refrescar cache
     */
    public static final int DEFAULT_CACHE_LIMIT_DAYS = 7;

    /**
     * Timeout por defecto para servicios en segundos
     */
    public static final int DEFAULT_SERVICE_TIMEOUT_SECONDS = 60;

    /**
     * Regex para caracteres prohibidos en sanitizacion
     */
    public static final String REGEX_FORBIDDEN_CHARACTERS =
        "[^a-zA-Z0-9\\-]";

    /**
     * Nombre del servicio de vehiculos
     */
    public static final String SERVICE_NAME_VEHICLE = "VEHICLE_QUERY";

    /**
     * Nombre del servicio de precios
     */
    public static final String SERVICE_NAME_PRICE = "PRICE_CALCULATION";

    /**
     * Rol requerido para consultar vehiculos
     */
    public static final String ROLE_GET_VEHICLES = "get_pn_vehiculos";
}
