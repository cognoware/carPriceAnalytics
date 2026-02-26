/**
 * carPriceAnalytics, Sistema de analisis de precios vehiculares
 * Copyright (c) 2025 Cognoware Cia. Ltda.
 * Todos los derechos reservados.
 * <p>
 * Este archivo forma parte del carPriceAnalytics de
 * Cognoware Cia. Ltda, desarrollado conforme a los requerimientos y
 * normativas establecidos.
 */
package ec.com.cognoware.carpriceanalytics.controller;

import ec.com.cognoware.carpriceanalytics.dto.output.VehicleQueryOutput;
import ec.com.cognoware.carpriceanalytics.service.interfaces.VehicleQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST para consulta de vehiculos y precios.
 *
 * Expone endpoints para buscar vehiculos por identificacion
 * o placa, ejecutando el proceso RPA y calculo de precios.
 *
 * @author JJARA
 * @version 1.0.0
 * @since 2025-02-26
 */
@RestController
@RequestMapping("/{realm}/vehicles/v1")
@Validated
@Tag(
    name = "Vehicle Query",
    description = "Consulta de vehiculos con proceso RPA y precios"
)
public class VehicleQueryController {

    private final VehicleQueryService vehicleQueryService;

    /**
     * Constructor con inyeccion de dependencias.
     *
     * @param vehicleQueryService servicio de consulta.
     */
    @Autowired
    public VehicleQueryController(
        VehicleQueryService vehicleQueryService
    ) {
        this.vehicleQueryService = vehicleQueryService;
    }

    /**
     * Busca vehiculos por identificacion del propietario.
     *
     * Ejecuta extraccion RPA y calculo de precios si los datos
     * en cache estan expirados o no existen.
     *
     * @param realm realm de Keycloak.
     * @param identification cedula o RUC del propietario.
     * @return lista de vehiculos con precios calculados.
     */
    @Operation(
        summary = "Buscar por identificacion",
        description = "Busca vehiculos por cedula o RUC del "
            + "propietario con proceso RPA y calculo de precios"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Consulta exitosa"
        ),
        @ApiResponse(
            responseCode = "401",
            description = "No autenticado"
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Sin permisos"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Vehiculo no encontrado"
        )
    })
    @RolesAllowed("get_pn_vehiculos")
    @GetMapping(
        value = "/identification/{identification}",
        produces = "application/json"
    )
    public ResponseEntity<VehicleQueryOutput> searchByIdentification(
        @Parameter(description = "Realm de Keycloak")
        @PathVariable(name = "realm") String realm,
        @Parameter(description = "Cedula o RUC del propietario")
        @PathVariable(name = "identification")
        @NotBlank
        @Size(min = 5, max = 20)
        String identification
    ) {
        VehicleQueryOutput result =
            vehicleQueryService.searchByIdentification(
                identification
            );
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Busca vehiculo por numero de placa.
     *
     * Ejecuta extraccion RPA y calculo de precios si los datos
     * en cache estan expirados o no existen.
     *
     * @param realm realm de Keycloak.
     * @param licensePlate numero de placa del vehiculo.
     * @return vehiculo con precios calculados.
     */
    @Operation(
        summary = "Buscar por placa",
        description = "Busca vehiculo por numero de placa "
            + "con proceso RPA y calculo de precios"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Consulta exitosa"
        ),
        @ApiResponse(
            responseCode = "401",
            description = "No autenticado"
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Sin permisos"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Vehiculo no encontrado"
        )
    })
    @RolesAllowed("get_pn_vehiculos")
    @GetMapping(
        value = "/plate/{licensePlate}",
        produces = "application/json"
    )
    public ResponseEntity<VehicleQueryOutput> searchByPlate(
        @Parameter(description = "Realm de Keycloak")
        @PathVariable(name = "realm") String realm,
        @Parameter(description = "Numero de placa del vehiculo")
        @PathVariable(name = "licensePlate")
        @NotBlank
        @Size(min = 3, max = 10)
        String licensePlate
    ) {
        VehicleQueryOutput result =
            vehicleQueryService.searchByPlate(licensePlate);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
