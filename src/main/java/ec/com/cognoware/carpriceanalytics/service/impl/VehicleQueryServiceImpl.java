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

import ec.com.cognoware.carpriceanalytics.dto.ExtractedVehicleData;
import ec.com.cognoware.carpriceanalytics.dto.output.VehicleQueryOutput;
import ec.com.cognoware.carpriceanalytics.mapper.VehicleMapper;
import ec.com.cognoware.carpriceanalytics.model.Vehicle;
import ec.com.cognoware.carpriceanalytics.repository.interfaces.VehicleRepository;
import ec.com.cognoware.carpriceanalytics.service.interfaces.PriceCalculationService;
import ec.com.cognoware.carpriceanalytics.service.interfaces.ServiceConfigService;
import ec.com.cognoware.carpriceanalytics.service.interfaces.VehicleExtractionService;
import ec.com.cognoware.carpriceanalytics.service.interfaces.VehicleQueryService;
import ec.com.cognoware.carpriceanalytics.util.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Orquestador principal de consulta de vehiculos.
 *
 * Implementa el flujo completo: cache -> consulta a proveedores ->
 * merge por prioridad -> calculo de precios -> persistencia.
 *
 * @author JJARA
 * @version 1.0.0
 * @since 2025-02-26
 */
@Service
public class VehicleQueryServiceImpl
    implements VehicleQueryService {

    private static final Logger log = LoggerFactory.getLogger(
        VehicleQueryServiceImpl.class
    );

    private final VehicleRepository vehicleRepository;
    private final VehicleExtractionService extractionService;
    private final PriceCalculationService priceCalcService;
    private final ServiceConfigService configService;
    private final FreshnessValidator freshnessValidator;
    private final VehicleMapper vehicleMapper;

    /**
     * Constructor con inyeccion de dependencias.
     *
     * @param vehicleRepository repositorio de vehiculos.
     * @param extractionService servicio de consulta a proveedores.
     * @param priceCalcService servicio de calculo de precios.
     * @param configService servicio de configuracion.
     * @param freshnessValidator validador de frescura.
     * @param vehicleMapper mapper de entidades a DTOs.
     */
    @Autowired
    public VehicleQueryServiceImpl(
        VehicleRepository vehicleRepository,
        VehicleExtractionService extractionService,
        PriceCalculationService priceCalcService,
        ServiceConfigService configService,
        FreshnessValidator freshnessValidator,
        VehicleMapper vehicleMapper
    ) {
        this.vehicleRepository = vehicleRepository;
        this.extractionService = extractionService;
        this.priceCalcService = priceCalcService;
        this.configService = configService;
        this.freshnessValidator = freshnessValidator;
        this.vehicleMapper = vehicleMapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VehicleQueryOutput searchByIdentification(
        String identification
    ) {
        log.info(
            "Buscando vehiculos por identificacion: {}",
            identification
        );
        List<Vehicle> cached = vehicleRepository
            .findByOwnerIdentification(identification);

        int limitDays = configService.getCacheLimitDays(
            AppConstants.SERVICE_NAME_VEHICLE
        );

        if (cached.isEmpty()
            || freshnessValidator.hasExpired(cached, limitDays)) {
            log.info(
                "Cache expirado o vacio, consultando proveedores"
            );
            List<Vehicle> refreshed =
                extractAndBuildVehicles(identification, null);
            if (!refreshed.isEmpty()) {
                return vehicleMapper.toQueryOutput(refreshed);
            }
        }

        return vehicleMapper.toQueryOutput(cached);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VehicleQueryOutput searchByPlate(String licensePlate) {
        log.info(
            "Buscando vehiculo por placa: {}",
            licensePlate
        );
        List<Vehicle> cached = vehicleRepository
            .findByLicensePlate(licensePlate);

        int limitDays = configService.getCacheLimitDays(
            AppConstants.SERVICE_NAME_VEHICLE
        );

        if (cached.isEmpty()
            || freshnessValidator.hasExpired(cached, limitDays)) {
            log.info(
                "Cache expirado o vacio, consultando proveedores"
            );
            List<Vehicle> refreshed =
                extractAndBuildVehicles(null, licensePlate);
            if (!refreshed.isEmpty()) {
                return vehicleMapper.toQueryOutput(refreshed);
            }
        }

        return vehicleMapper.toQueryOutput(cached);
    }

    private List<Vehicle> extractAndBuildVehicles(
        String identification,
        String licensePlate
    ) {
        String plateToSearch = licensePlate != null
            ? licensePlate : "UNKNOWN";

        List<ExtractedVehicleData> extractedList =
            extractionService.extractAllByPlate(plateToSearch);

        if (extractedList.isEmpty()) {
            log.warn("No se obtuvieron datos de extraccion");
            return List.of();
        }

        Vehicle vehicle = buildVehicleFromExtracted(
            extractedList
        );

        if (identification != null) {
            vehicle.setOwnerIdentification(identification);
        }
        vehicle.setCreatedDate(LocalDateTime.now());

        priceCalcService.calculateAndApplyPrices(vehicle);

        vehicle = vehicleRepository.save(vehicle);

        log.info(
            "Vehiculo guardado en cache con id: {}",
            vehicle.getId()
        );

        List<Vehicle> result = new ArrayList<>();
        result.add(vehicle);
        return result;
    }

    private Vehicle buildVehicleFromExtracted(
        List<ExtractedVehicleData> extractedList
    ) {
        Vehicle vehicle = new Vehicle();

        for (ExtractedVehicleData data : extractedList) {
            vehicleMapper.applyExtractedData(vehicle, data);
        }

        return vehicle;
    }
}
