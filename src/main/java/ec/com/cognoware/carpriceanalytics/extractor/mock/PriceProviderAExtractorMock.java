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
 * Mock del extractor de precios del Proveedor A para demo.
 *
 * Simula el consumo del servicio de precios del Proveedor A
 * con datos de ejemplo basados en marca, modelo y anio.
 *
 * @author JJARA
 * @version 1.0.0
 * @since 2025-02-26
 */
@Component
public class PriceProviderAExtractorMock
    implements VehiclePriceExtractor {

    private static final Logger log = LoggerFactory.getLogger(
        PriceProviderAExtractorMock.class
    );

    private static final int SIMULATED_DELAY_MS = 2500;
    private static final BigDecimal BASE_PRICE =
        new BigDecimal("12500.00");
    private static final BigDecimal DEPRECIATION_RATE =
        new BigDecimal("0.08");
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
            "Proveedor Precios A Mock: calculando precio para {} {} {}",
            vehicle.getBrand(),
            vehicle.getModel(),
            vehicle.getVehicleYear()
        );
        simulateDelay();

        BigDecimal avgPrice = calculateMockPrice(vehicle);
        BigDecimal minPrice = avgPrice.multiply(
            new BigDecimal("0.85")
        );
        BigDecimal maxPrice = avgPrice.multiply(
            new BigDecimal("1.15")
        );

        PriceDetailOutput price = PriceDetailOutput.builder()
            .averagePrice(avgPrice)
            .minPrice(minPrice)
            .maxPrice(maxPrice)
            .source(getSourceName())
            .calculationDate(LocalDateTime.now())
            .build();

        log.info(
            "Proveedor Precios A Mock: precio calculado ${} para {} {}",
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
        return "PRICE_PROVIDER_A";
    }

    private BigDecimal calculateMockPrice(Vehicle vehicle) {
        int yearsOld = REFERENCE_YEAR
            - (vehicle.getVehicleYear() != null
                ? vehicle.getVehicleYear() : REFERENCE_YEAR);
        if (yearsOld < 0) {
            yearsOld = 0;
        }
        BigDecimal depreciation = DEPRECIATION_RATE.multiply(
            new BigDecimal(yearsOld)
        );
        BigDecimal factor = BigDecimal.ONE.subtract(depreciation);
        if (factor.compareTo(new BigDecimal("0.20")) < 0) {
            factor = new BigDecimal("0.20");
        }
        return BASE_PRICE.multiply(factor);
    }

    /**
     * Simula latencia de red en la consulta al proveedor.
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
