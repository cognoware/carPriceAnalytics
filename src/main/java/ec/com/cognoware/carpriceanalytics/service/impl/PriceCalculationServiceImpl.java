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

import ec.com.cognoware.carpriceanalytics.dto.output.PriceDetailOutput;
import ec.com.cognoware.carpriceanalytics.extractor.interfaces.VehiclePriceExtractor;
import ec.com.cognoware.carpriceanalytics.model.Vehicle;
import ec.com.cognoware.carpriceanalytics.model.VehicleHomologation;
import ec.com.cognoware.carpriceanalytics.repository.interfaces.VehicleHomologationRepository;
import ec.com.cognoware.carpriceanalytics.service.interfaces.PriceCalculationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * Calcula precios vehiculares desde multiples fuentes.
 *
 * Realiza homologacion del modelo comercial y consulta
 * precios en paralelo desde proveedores habilitados.
 *
 * @author JJARA
 * @version 1.0.0
 * @since 2025-02-26
 */
@Service
public class PriceCalculationServiceImpl
    implements PriceCalculationService {

    private static final Logger log = LoggerFactory.getLogger(
        PriceCalculationServiceImpl.class
    );

    private final List<VehiclePriceExtractor> priceExtractors;
    private final VehicleHomologationRepository homologationRepo;

    @Value("${extraction.timeout.price:20000}")
    private long priceTimeoutMs;

    /**
     * Constructor con inyeccion de dependencias.
     *
     * @param priceExtractors lista de extractores de precios.
     * @param homologationRepo repositorio de homologacion.
     */
    @Autowired
    public PriceCalculationServiceImpl(
        List<VehiclePriceExtractor> priceExtractors,
        VehicleHomologationRepository homologationRepo
    ) {
        this.priceExtractors = priceExtractors;
        this.homologationRepo = homologationRepo;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vehicle calculateAndApplyPrices(Vehicle vehicle) {
        log.info(
            "Calculando precios para {} {} {}",
            vehicle.getBrand(),
            vehicle.getModel(),
            vehicle.getVehicleYear()
        );

        applyHomologation(vehicle);

        List<CompletableFuture<PriceDetailOutput>> futures =
            priceExtractors.stream()
                .map(ext -> ext.extractPrice(vehicle)
                    .exceptionally(ex -> {
                        log.warn(
                            "Extractor precio {} fallo: {}",
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
            allFutures.get(priceTimeoutMs, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            log.warn(
                "Timeout en calculo de precios: {}",
                e.getMessage()
            );
        }

        List<PriceDetailOutput> prices = futures.stream()
            .map(f -> {
                try {
                    return f.getNow(null);
                } catch (Exception e) {
                    return null;
                }
            })
            .filter(Objects::nonNull)
            .filter(p -> p.getAveragePrice() != null)
            .toList();

        if (!prices.isEmpty()) {
            applyAveragePrices(vehicle, prices);
        }

        return vehicle;
    }

    private void applyHomologation(Vehicle vehicle) {
        if (vehicle.getBrand() == null
            || vehicle.getModel() == null) {
            return;
        }
        List<VehicleHomologation> homologations =
            homologationRepo.findByBrandAndModel(
                vehicle.getBrand(),
                vehicle.getModel()
            );
        if (!homologations.isEmpty()) {
            VehicleHomologation h = homologations.getFirst();
            vehicle.setCommercialModel(h.getCommercialModel());
            log.info(
                "Homologacion aplicada: {} -> {}",
                vehicle.getModel(),
                h.getCommercialModel()
            );
        }
    }

    private void applyAveragePrices(
        Vehicle vehicle,
        List<PriceDetailOutput> prices
    ) {
        BigDecimal avgSum = BigDecimal.ZERO;
        BigDecimal minSum = BigDecimal.ZERO;
        BigDecimal maxSum = BigDecimal.ZERO;
        int count = prices.size();
        List<String> sources = new java.util.ArrayList<>();

        for (PriceDetailOutput p : prices) {
            avgSum = avgSum.add(p.getAveragePrice());
            if (p.getMinPrice() != null) {
                minSum = minSum.add(p.getMinPrice());
            }
            if (p.getMaxPrice() != null) {
                maxSum = maxSum.add(p.getMaxPrice());
            }
            sources.add(p.getSource());
        }

        BigDecimal divisor = new BigDecimal(count);
        vehicle.setAveragePrice(
            avgSum.divide(divisor, 2, RoundingMode.HALF_UP)
        );
        vehicle.setMinPrice(
            minSum.divide(divisor, 2, RoundingMode.HALF_UP)
        );
        vehicle.setMaxPrice(
            maxSum.divide(divisor, 2, RoundingMode.HALF_UP)
        );
        vehicle.setPriceSource(String.join(",", sources));
        vehicle.setLastUpdateDate(LocalDateTime.now());

        log.info(
            "Precios calculados: avg={}, min={}, max={} de {}",
            vehicle.getAveragePrice(),
            vehicle.getMinPrice(),
            vehicle.getMaxPrice(),
            vehicle.getPriceSource()
        );
    }
}
