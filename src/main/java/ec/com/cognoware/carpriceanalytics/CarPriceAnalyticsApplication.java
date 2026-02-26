/**
 * carPriceAnalytics, Sistema de analisis de precios vehiculares
 * Copyright (c) 2025 Cognoware Cia. Ltda.
 * Todos los derechos reservados.
 * <p>
 * Este archivo forma parte del carPriceAnalytics de
 * Cognoware Cia. Ltda, desarrollado conforme a los requerimientos y
 * normativas establecidos.
 */
package ec.com.cognoware.carpriceanalytics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de la aplicacion Spring Boot.
 *
 * Inicia el sistema de analisis de precios vehiculares con
 * extraccion RPA y calculo de valores comerciales.
 *
 * @author JJARA
 * @version 1.0.0
 * @since 2025-02-26
 */
@SpringBootApplication
public class CarPriceAnalyticsApplication {

    /**
     * Punto de entrada principal de la aplicacion.
     *
     * @param args argumentos de linea de comandos.
     */
    public static void main(String[] args) {
        SpringApplication.run(
            CarPriceAnalyticsApplication.class, args
        );
    }
}
