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

/**
 * Servicio de configuracion de servicios externos.
 *
 * Obtiene timeouts y limites de cache desde base de datos.
 *
 * @author JJARA
 * @version 1.0.0
 * @since 2025-02-26
 */
public interface ServiceConfigService {

    /**
     * Obtiene el timeout en segundos para un servicio.
     *
     * @param serviceName nombre del servicio.
     * @return timeout en segundos.
     */
    int getTimeoutSeconds(String serviceName);

    /**
     * Obtiene el limite de dias para cache de un servicio.
     *
     * @param serviceName nombre del servicio.
     * @return limite de dias para refrescar cache.
     */
    int getCacheLimitDays(String serviceName);
}
