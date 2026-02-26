/**
 * carPriceAnalytics, Sistema de analisis de precios vehiculares
 * Copyright (c) 2025 Cognoware Cia. Ltda.
 * Todos los derechos reservados.
 * <p>
 * Este archivo forma parte del carPriceAnalytics de
 * Cognoware Cia. Ltda, desarrollado conforme a los requerimientos y
 * normativas establecidos.
 */
package ec.com.cognoware.carpriceanalytics.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * DTO interno con datos extraidos de fuentes externas (RPA).
 *
 * Contiene la prioridad del extractor para el merge de datos.
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
public class ExtractedVehicleData implements Serializable {

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
    private String sourceName;
    private int orderPriority;
}
