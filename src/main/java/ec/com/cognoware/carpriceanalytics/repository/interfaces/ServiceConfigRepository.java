/**
 * carPriceAnalytics, Sistema de analisis de precios vehiculares
 * Copyright (c) 2025 Cognoware Cia. Ltda.
 * Todos los derechos reservados.
 * <p>
 * Este archivo forma parte del carPriceAnalytics de
 * Cognoware Cia. Ltda, desarrollado conforme a los requerimientos y
 * normativas establecidos.
 */
package ec.com.cognoware.carpriceanalytics.repository.interfaces;

import ec.com.cognoware.carpriceanalytics.model.ServiceConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para configuracion de servicios externos.
 *
 * @author JJARA
 * @version 1.0.0
 * @since 2025-02-26
 */
@Repository
public interface ServiceConfigRepository
    extends JpaRepository<ServiceConfig, Long> {

    /**
     * Busca configuracion por nombre del servicio.
     *
     * @param serviceName nombre del servicio.
     * @return configuracion del servicio si existe.
     */
    Optional<ServiceConfig> findByServiceName(String serviceName);
}
