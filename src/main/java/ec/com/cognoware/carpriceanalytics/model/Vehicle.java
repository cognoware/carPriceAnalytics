/**
 * carPriceAnalytics, Sistema de analisis de precios vehiculares
 * Copyright (c) 2025 Cognoware Cia. Ltda.
 * Todos los derechos reservados.
 * <p>
 * Este archivo forma parte del carPriceAnalytics de
 * Cognoware Cia. Ltda, desarrollado conforme a los requerimientos y
 * normativas establecidos.
 */
package ec.com.cognoware.carpriceanalytics.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad que representa un vehiculo con sus datos y precios.
 *
 * @author JJARA
 * @version 1.0.0
 * @since 2025-02-26
 */
@Entity
@Table(name = "vehicle")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "license_plate", length = 20)
    private String licensePlate;

    @Column(name = "owner_identification", length = 20)
    private String ownerIdentification;

    @Column(name = "owner_name", length = 200)
    private String ownerName;

    @Column(name = "brand", length = 100)
    private String brand;

    @Column(name = "model", length = 100)
    private String model;

    @Column(name = "vehicle_year")
    private Integer vehicleYear;

    @Column(name = "color", length = 50)
    private String color;

    @Column(name = "engine_displacement", length = 20)
    private String engineDisplacement;

    @Column(name = "vehicle_class", length = 100)
    private String vehicleClass;

    @Column(name = "service_type", length = 50)
    private String serviceType;

    @Column(name = "vin", length = 50)
    private String vin;

    @Column(name = "canton", length = 100)
    private String canton;

    @Column(name = "country", length = 100)
    private String country;

    @Column(name = "commercial_model", length = 200)
    private String commercialModel;

    @Column(name = "average_price", precision = 12, scale = 2)
    private BigDecimal averagePrice;

    @Column(name = "min_price", precision = 12, scale = 2)
    private BigDecimal minPrice;

    @Column(name = "max_price", precision = 12, scale = 2)
    private BigDecimal maxPrice;

    @Column(name = "price_source", length = 100)
    private String priceSource;

    @Column(name = "last_update_date")
    private LocalDateTime lastUpdateDate;

    @Column(name = "created_date")
    private LocalDateTime createdDate;
}
