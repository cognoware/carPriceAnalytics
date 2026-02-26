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
import ec.com.cognoware.carpriceanalytics.extractor.interfaces.VehicleDataExtractor;
import ec.com.cognoware.carpriceanalytics.service.interfaces.VehicleExtractionService;
import ec.com.cognoware.carpriceanalytics.util.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * Orquesta la extraccion paralela de datos vehiculares.
 *
 * Lanza todas las fuentes en paralelo con CompletableFuture,
 * aplica timeout y ordena resultados por prioridad.
 *
 * @author JJARA
 * @version 1.0.0
 * @since 2025-02-26
 */
@Service
public class VehicleExtractionServiceImpl
    implements VehicleExtractionService {

    private static final Logger log = LoggerFactory.getLogger(
        VehicleExtractionServiceImpl.class
    );

    private final List<VehicleDataExtractor> extractors;

    @Value("${extraction.timeout.default:25000}")
    private long extractionTimeoutMs;

    /**
     * Constructor con inyeccion de todos los extractores.
     *
     * @param extractors lista de extractores disponibles.
     */
    @Autowired
    public VehicleExtractionServiceImpl(
        List<VehicleDataExtractor> extractors
    ) {
        this.extractors = extractors;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ExtractedVehicleData> extractAllByPlate(
        String licensePlate
    ) {
        log.info(
            "Iniciando extraccion paralela para placa: {}",
            licensePlate
        );

        List<CompletableFuture<ExtractedVehicleData>> futures =
            extractors.stream()
                .map(ext -> ext.extractByPlate(licensePlate)
                    .exceptionally(ex -> {
                        log.warn(
                            "Extractor {} fallo: {}",
                            ext.getSourceName(),
                            ex.getMessage()
                        );
                        return null;
                    }))
                .toList();

        CompletableFuture<Void> allFutures =
            CompletableFuture.allOf(
                futures.toArray(new CompletableFuture[0])
            );

        try {
            allFutures.get(
                extractionTimeoutMs,
                TimeUnit.MILLISECONDS
            );
        } catch (Exception e) {
            log.warn(
                "Timeout en extraccion paralela: {}",
                e.getMessage()
            );
        }

        List<ExtractedVehicleData> results = futures.stream()
            .map(f -> {
                try {
                    return f.getNow(null);
                } catch (Exception e) {
                    return null;
                }
            })
            .filter(Objects::nonNull)
            .sorted(Comparator.comparingInt(
                ExtractedVehicleData::getOrderPriority
            ))
            .toList();

        log.info(
            "Extraccion completada: {} fuentes respondieron",
            results.size()
        );
        return results;
    }
}
