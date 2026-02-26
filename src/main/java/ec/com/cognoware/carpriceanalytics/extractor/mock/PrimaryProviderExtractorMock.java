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
 * Mock del extractor del proveedor primario para demo.
 *
 * Simula la respuesta del servicio del proveedor primario
 * con datos de ejemplo y latencia artificial.
 *
 * @author JJARA
 * @version 1.0.0
 * @since 2025-02-26
 */
@Component
public class PrimaryProviderExtractorMock
    implements VehicleDataExtractor {

    private static final Logger log =
        LoggerFactory.getLogger(PrimaryProviderExtractorMock.class);

    private static final int SIMULATED_DELAY_MS = 2000;

    /**
     * {@inheritDoc}
     */
    @Override
    @Async
    public CompletableFuture<ExtractedVehicleData> extractByPlate(
        String licensePlate
    ) {
        log.info(
            "Proveedor Primario Mock: extrayendo datos para placa {}",
            licensePlate
        );
        simulateDelay();

        ExtractedVehicleData data = ExtractedVehicleData.builder()
            .licensePlate(licensePlate)
            .ownerIdentification("0102030405")
            .ownerName("JUAN CARLOS PEREZ LOPEZ")
            .brand("CHEVROLET")
            .model("AVEO FAMILY")
            .vehicleYear(2018)
            .color("BLANCO")
            .engineDisplacement("1500")
            .vehicleClass("AUTOMOVIL")
            .serviceType("PARTICULAR")
            .vin("9GASP48X09B123456")
            .canton("CUENCA")
            .country("ECUADOR")
            .sourceName(getSourceName())
            .orderPriority(getPriority())
            .build();

        log.info(
            "Proveedor Primario Mock: datos extraidos para placa {}",
            licensePlate
        );
        return CompletableFuture.completedFuture(data);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getPriority() {
        return AppConstants.ORDER_PRIORITY_ONE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSourceName() {
        return "PRIMARY_PROVIDER";
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
