/**
 * carPriceAnalytics, Sistema de analisis de precios vehiculares
 * Copyright (c) 2025 Cognoware Cia. Ltda.
 * Todos los derechos reservados.
 * <p>
 * Este archivo forma parte del carPriceAnalytics de
 * Cognoware Cia. Ltda, desarrollado conforme a los requerimientos y
 * normativas establecidos.
 */
package ec.com.cognoware.carpriceanalytics.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuracion de OpenAPI 3 (Swagger) para documentacion.
 *
 * @author JJARA
 * @version 1.0.0
 * @since 2025-02-26
 */
@Configuration
public class SwaggerConfig {

    private static final String SECURITY_SCHEME_NAME = "Bearer";

    /**
     * Configura la documentacion OpenAPI con seguridad JWT.
     *
     * @return configuracion de OpenAPI.
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Car Price Analytics API")
                .version("1.0.0")
                .description(
                    "API de consulta de vehiculos y calculo "
                    + "de precios mediante proceso RPA"
                )
                .contact(new Contact()
                    .name("Cognoware Cia. Ltda.")
                    .email("info@cognoware.com.ec")))
            .addSecurityItem(
                new SecurityRequirement()
                    .addList(SECURITY_SCHEME_NAME)
            )
            .components(new Components()
                .addSecuritySchemes(
                    SECURITY_SCHEME_NAME,
                    new SecurityScheme()
                        .name(SECURITY_SCHEME_NAME)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                ));
    }
}
