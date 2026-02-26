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

import ec.com.cognoware.carpriceanalytics.model.ServiceConfig;
import ec.com.cognoware.carpriceanalytics.repository.interfaces.ServiceConfigRepository;
import ec.com.cognoware.carpriceanalytics.service.interfaces.ServiceConfigService;
import ec.com.cognoware.carpriceanalytics.util.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementacion del servicio de configuracion.
 *
 * Obtiene parametros de configuracion desde la base de datos
 * con valores por defecto como fallback.
 *
 * @author JJARA
 * @version 1.0.0
 * @since 2025-02-26
 */
@Service
public class ServiceConfigServiceImpl
    implements ServiceConfigService {

    private final ServiceConfigRepository repository;

    /**
     * Constructor con inyeccion de dependencias.
     *
     * @param repository repositorio de configuracion.
     */
    @Autowired
    public ServiceConfigServiceImpl(
        ServiceConfigRepository repository
    ) {
        this.repository = repository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getTimeoutSeconds(String serviceName) {
        return repository.findByServiceName(serviceName)
            .map(ServiceConfig::getTimeoutSeconds)
            .orElse(AppConstants.DEFAULT_SERVICE_TIMEOUT_SECONDS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCacheLimitDays(String serviceName) {
        return repository.findByServiceName(serviceName)
            .map(ServiceConfig::getCacheLimitDays)
            .orElse(AppConstants.DEFAULT_CACHE_LIMIT_DAYS);
    }
}
