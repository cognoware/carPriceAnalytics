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

import ec.com.cognoware.carpriceanalytics.model.VehicleHomologation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para la homologacion de vehiculos.
 *
 * @author JJARA
 * @version 1.0.0
 * @since 2025-02-26
 */
@Repository
public interface VehicleHomologationRepository
    extends JpaRepository<VehicleHomologation, Long> {

    /**
     * Busca homologaciones por marca y modelo.
     *
     * @param brand marca del vehiculo.
     * @param model modelo del vehiculo.
     * @return lista de homologaciones encontradas.
     */
    List<VehicleHomologation> findByBrandAndModel(
        String brand,
        String model
    );
}
