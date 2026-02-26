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

import ec.com.cognoware.carpriceanalytics.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para operaciones de persistencia de vehiculos.
 *
 * @author JJARA
 * @version 1.0.0
 * @since 2025-02-26
 */
@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    /**
     * Busca vehiculos por numero de placa.
     *
     * @param licensePlate numero de placa del vehiculo.
     * @return lista de vehiculos encontrados.
     */
    List<Vehicle> findByLicensePlate(String licensePlate);

    /**
     * Busca vehiculos por identificacion del propietario.
     *
     * @param ownerIdentification cedula o RUC del propietario.
     * @return lista de vehiculos del propietario.
     */
    List<Vehicle> findByOwnerIdentification(
        String ownerIdentification
    );
}
