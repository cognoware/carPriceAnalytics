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

/**
 * Entidad de configuracion de servicios externos (timeouts, cache).
 *
 * @author JJARA
 * @version 1.0.0
 * @since 2025-02-26
 */
@Entity
@Table(name = "service_config")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceConfig implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "service_name", length = 100, unique = true)
    private String serviceName;

    @Column(name = "timeout_seconds")
    private Integer timeoutSeconds;

    @Column(name = "cache_limit_days")
    private Integer cacheLimitDays;

    @Column(name = "active")
    private Boolean active;
}
