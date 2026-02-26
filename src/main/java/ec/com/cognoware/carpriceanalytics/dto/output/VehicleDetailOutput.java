/**
 * carPriceAnalytics, Sistema de analisis de precios vehiculares
 * Copyright (c) 2025 Cognoware Cia. Ltda.
 * Todos los derechos reservados.
 * <p>
 * Este archivo forma parte del carPriceAnalytics de
 * Cognoware Cia. Ltda, desarrollado conforme a los requerimientos y
 * normativas establecidos.
 */
package ec.com.cognoware.carpriceanalytics.dto.output;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO con el detalle completo de un vehiculo y sus precios.
 *
 * @author JJARA
 * @version 1.0.0
 * @since 2025-02-26
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VehicleDetailOutput implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String licensePlate;
    private String ownerIdentification;
    private String ownerName;
    private String brand;
    private String model;
    private Integer vehicleYear;
    private String color;
    private String engineDisplacement;
    private String vehicleClass;
    private String serviceType;
    private String vin;
    private String canton;
    private String country;
    private String commercialModel;
    private PriceDetailOutput priceDetail;
}
