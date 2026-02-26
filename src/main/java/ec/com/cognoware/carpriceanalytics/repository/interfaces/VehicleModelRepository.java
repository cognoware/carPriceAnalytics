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

import ec.com.cognoware.carpriceanalytics.model.VehicleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para modelos de vehiculos.
 *
 * @author JJARA
 * @version 1.0.0
 * @since 2025-02-26
 */
@Repository
public interface VehicleModelRepository
    extends JpaRepository<VehicleModel, Long> {

    /**
     * Busca un modelo por marca y modelo.
     *
     * @param brand marca del vehiculo.
     * @param model modelo del vehiculo.
     * @return modelo encontrado si existe.
     */
    Optional<VehicleModel> findByBrandAndModel(
        String brand,
        String model
    );
}
