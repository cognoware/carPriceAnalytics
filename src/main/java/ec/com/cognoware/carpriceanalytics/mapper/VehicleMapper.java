/**
 * carPriceAnalytics, Sistema de analisis de precios vehiculares
 * Copyright (c) 2025 Cognoware Cia. Ltda.
 * Todos los derechos reservados.
 * <p>
 * Este archivo forma parte del carPriceAnalytics de
 * Cognoware Cia. Ltda, desarrollado conforme a los requerimientos y
 * normativas establecidos.
 */
package ec.com.cognoware.carpriceanalytics.mapper;

import ec.com.cognoware.carpriceanalytics.dto.ExtractedVehicleData;
import ec.com.cognoware.carpriceanalytics.dto.output.PriceDetailOutput;
import ec.com.cognoware.carpriceanalytics.dto.output.VehicleDetailOutput;
import ec.com.cognoware.carpriceanalytics.dto.output.VehicleQueryOutput;
import ec.com.cognoware.carpriceanalytics.model.Vehicle;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Mapper para convertir entre entidades Vehicle y DTOs.
 *
 * @author JJARA
 * @version 1.0.0
 * @since 2025-02-26
 */
@Component
public class VehicleMapper {

    /**
     * Convierte una entidad Vehicle a VehicleDetailOutput.
     *
     * @param entity entidad JPA del vehiculo.
     * @return DTO de detalle del vehiculo.
     */
    public VehicleDetailOutput toDetailOutput(Vehicle entity) {
        if (entity == null) {
            return null;
        }

        PriceDetailOutput priceDetail = null;
        if (entity.getAveragePrice() != null) {
            priceDetail = PriceDetailOutput.builder()
                .averagePrice(entity.getAveragePrice())
                .minPrice(entity.getMinPrice())
                .maxPrice(entity.getMaxPrice())
                .source(entity.getPriceSource())
                .calculationDate(entity.getLastUpdateDate())
                .build();
        }

        return VehicleDetailOutput.builder()
            .licensePlate(entity.getLicensePlate())
            .ownerIdentification(entity.getOwnerIdentification())
            .ownerName(entity.getOwnerName())
            .brand(entity.getBrand())
            .model(entity.getModel())
            .vehicleYear(entity.getVehicleYear())
            .color(entity.getColor())
            .engineDisplacement(entity.getEngineDisplacement())
            .vehicleClass(entity.getVehicleClass())
            .serviceType(entity.getServiceType())
            .vin(entity.getVin())
            .canton(entity.getCanton())
            .country(entity.getCountry())
            .commercialModel(entity.getCommercialModel())
            .priceDetail(priceDetail)
            .build();
    }

    /**
     * Convierte una lista de entidades a VehicleQueryOutput.
     *
     * @param entities lista de entidades Vehicle.
     * @return DTO wrapper con la lista de vehiculos.
     */
    public VehicleQueryOutput toQueryOutput(
        List<Vehicle> entities
    ) {
        List<VehicleDetailOutput> details = entities.stream()
            .map(this::toDetailOutput)
            .toList();
        return new VehicleQueryOutput(details, null);
    }

    /**
     * Aplica datos extraidos a una entidad Vehicle existente.
     *
     * Solo sobreescribe campos nulos del vehiculo con los datos
     * del extractor, respetando la prioridad de la fuente.
     *
     * @param entity entidad Vehicle a enriquecer.
     * @param data datos extraidos de la fuente externa.
     * @return entidad Vehicle actualizada.
     */
    public Vehicle applyExtractedData(
        Vehicle entity,
        ExtractedVehicleData data
    ) {
        if (data == null) {
            return entity;
        }
        if (entity.getLicensePlate() == null) {
            entity.setLicensePlate(data.getLicensePlate());
        }
        if (entity.getOwnerIdentification() == null) {
            entity.setOwnerIdentification(
                data.getOwnerIdentification()
            );
        }
        if (entity.getOwnerName() == null) {
            entity.setOwnerName(data.getOwnerName());
        }
        if (entity.getBrand() == null) {
            entity.setBrand(data.getBrand());
        }
        if (entity.getModel() == null) {
            entity.setModel(data.getModel());
        }
        if (entity.getVehicleYear() == null) {
            entity.setVehicleYear(data.getVehicleYear());
        }
        if (entity.getColor() == null) {
            entity.setColor(data.getColor());
        }
        if (entity.getEngineDisplacement() == null) {
            entity.setEngineDisplacement(
                data.getEngineDisplacement()
            );
        }
        if (entity.getVehicleClass() == null) {
            entity.setVehicleClass(data.getVehicleClass());
        }
        if (entity.getServiceType() == null) {
            entity.setServiceType(data.getServiceType());
        }
        if (entity.getVin() == null) {
            entity.setVin(data.getVin());
        }
        if (entity.getCanton() == null) {
            entity.setCanton(data.getCanton());
        }
        if (entity.getCountry() == null) {
            entity.setCountry(data.getCountry());
        }
        return entity;
    }
}
