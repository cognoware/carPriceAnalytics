/**
 * carPriceAnalytics, Sistema de analisis de precios vehiculares
 * Copyright (c) 2025 Cognoware Cia. Ltda.
 * Todos los derechos reservados.
 * <p>
 * Este archivo forma parte del carPriceAnalytics de
 * Cognoware Cia. Ltda, desarrollado conforme a los requerimientos y
 * normativas establecidos.
 */
package ec.com.cognoware.carpriceanalytics.extractor.mock;

import ec.com.cognoware.carpriceanalytics.dto.output.PriceDetailOutput;
import ec.com.cognoware.carpriceanalytics.extractor.interfaces.VehiclePriceExtractor;
import ec.com.cognoware.carpriceanalytics.model.Vehicle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

/**
 * Mock del extractor de precios Generali para demo.
 *
 * Simula la consulta de precios de aseguradora Generali
 * con datos de ejemplo.
 *
 * @author JJARA
 * @version 1.0.0
 * @since 2025-02-26
 */
@Component
public class GeneraliPriceExtractorMock
    implements VehiclePriceExtractor {

    private static final Logger log = LoggerFactory.getLogger(
        GeneraliPriceExtractorMock.class
    );

    private static final int SIMULATED_DELAY_MS = 1800;
    private static final BigDecimal BASE_INSURED_VALUE =
        new BigDecimal("13000.00");
    private static final BigDecimal YEARLY_DEPRECIATION =
        new BigDecimal("0.07");
    private static final int REFERENCE_YEAR = 2025;

    /**
     * {@inheritDoc}
     */
    @Override
    @Async
    public CompletableFuture<PriceDetailOutput> extractPrice(
        Vehicle vehicle
    ) {
        log.info(
            "Generali Mock: calculando precio para {} {} {}",
            vehicle.getBrand(),
            vehicle.getModel(),
            vehicle.getVehicleYear()
        );
        simulateDelay();

        BigDecimal avgPrice = calculateMockPrice(vehicle);
        BigDecimal minPrice = avgPrice.multiply(
            new BigDecimal("0.90")
        );
        BigDecimal maxPrice = avgPrice.multiply(
            new BigDecimal("1.10")
        );

        PriceDetailOutput price = PriceDetailOutput.builder()
            .averagePrice(avgPrice)
            .minPrice(minPrice)
            .maxPrice(maxPrice)
            .source(getSourceName())
            .calculationDate(LocalDateTime.now())
            .build();

        log.info(
            "Generali Mock: precio calculado ${} para {} {}",
            avgPrice,
            vehicle.getBrand(),
            vehicle.getModel()
        );
        return CompletableFuture.completedFuture(price);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSourceName() {
        return "GENERALI";
    }

    private BigDecimal calculateMockPrice(Vehicle vehicle) {
        int yearsOld = REFERENCE_YEAR
            - (vehicle.getVehicleYear() != null
                ? vehicle.getVehicleYear() : REFERENCE_YEAR);
        if (yearsOld < 0) {
            yearsOld = 0;
        }
        BigDecimal depreciation = YEARLY_DEPRECIATION.multiply(
            new BigDecimal(yearsOld)
        );
        BigDecimal factor = BigDecimal.ONE.subtract(depreciation);
        if (factor.compareTo(new BigDecimal("0.25")) < 0) {
            factor = new BigDecimal("0.25");
        }
        return BASE_INSURED_VALUE.multiply(factor);
    }

    /**
     * Simula latencia de red en la consulta Generali.
     *
     * Uso de try-catch: necesario para manejar
     * InterruptedException del Thread.sleep() y restaurar
     * el estado de interrupcion del hilo.
     */
    private void simulateDelay() {
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
