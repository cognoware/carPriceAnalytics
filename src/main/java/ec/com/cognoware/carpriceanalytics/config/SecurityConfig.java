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

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Configuracion de seguridad con OAuth2 Resource Server (Keycloak).
 *
 * Configura JWT como mecanismo de autenticacion y extrae roles
 * del claim realm_access del token Keycloak.
 *
 * @author JJARA
 * @version 1.0.0
 * @since 2025-02-26
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true)
public class SecurityConfig {

    /**
     * Configura la cadena de filtros de seguridad HTTP.
     *
     * @param http configurador de seguridad HTTP.
     * @return cadena de filtros configurada.
     * @throws Exception si falla la configuracion.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(
        HttpSecurity http
    ) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session
                .sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                ))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/health",
                    "/h2-console/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/v3/api-docs/**"
                ).permitAll()
                .anyRequest().authenticated()
            )
            .headers(headers -> headers
                .frameOptions(frame -> frame.sameOrigin())
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt
                    .jwtAuthenticationConverter(
                        jwtAuthenticationConverter()
                    ))
            );

        return http.build();
    }

    /**
     * Convierte claims JWT de Keycloak a authorities de Spring.
     *
     * @return converter configurado para Keycloak.
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter =
            new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(
            new KeycloakRealmRoleConverter()
        );
        return converter;
    }

    /**
     * Extrae roles del claim realm_access.roles del JWT Keycloak.
     */
    private static class KeycloakRealmRoleConverter
        implements Converter<Jwt, Collection<GrantedAuthority>> {

        @Override
        @SuppressWarnings("unchecked")
        public Collection<GrantedAuthority> convert(Jwt jwt) {
            Map<String, Object> realmAccess =
                jwt.getClaimAsMap("realm_access");
            if (realmAccess == null
                || !realmAccess.containsKey("roles")) {
                return Collections.emptyList();
            }
            List<String> roles =
                (List<String>) realmAccess.get("roles");
            return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        }
    }
}
