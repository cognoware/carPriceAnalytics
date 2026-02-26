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

import ec.com.cognoware.carpriceanalytics.dto.ExtractedVehicleData;
import ec.com.cognoware.carpriceanalytics.extractor.interfaces.VehicleDataExtractor;
import ec.com.cognoware.carpriceanalytics.util.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

/**
 * Mock del extractor del proveedor terciario para demo.
 *
 * Simula la respuesta del servicio del proveedor terciario.
 *
 * @author JJARA
 * @version 1.0.0
 * @since 2025-02-26
 */
@Component
public class TertiaryProviderExtractorMock
    implements VehicleDataExtractor {

    private static final Logger log =
        LoggerFactory.getLogger(TertiaryProviderExtractorMock.class);

    private static final int SIMULATED_DELAY_MS = 3000;

    /**
     * {@inheritDoc}
     */
    @Override
    @Async
    public CompletableFuture<ExtractedVehicleData> extractByPlate(
        String licensePlate
    ) {
        log.info(
            "Proveedor Terciario Mock: extrayendo datos para placa {}",
            licensePlate
        );
        simulateDelay();

        ExtractedVehicleData data = ExtractedVehicleData.builder()
            .licensePlate(licensePlate)
            .brand("CHEVROLET")
            .model("AVEO FAMILY")
            .vehicleYear(2018)
            .canton("CUENCA")
            .sourceName(getSourceName())
            .orderPriority(getPriority())
            .build();

        log.info(
            "Proveedor Terciario Mock: datos extraidos para placa {}",
            licensePlate
        );
        return CompletableFuture.completedFuture(data);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getPriority() {
        return AppConstants.ORDER_PRIORITY_FOUR;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSourceName() {
        return "TERTIARY_PROVIDER";
    }

    /**
     * Simula latencia de red en la consulta al proveedor terciario.
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
