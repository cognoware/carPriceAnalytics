# carPriceAnalytics

**Sistema de Analisis de Precios Vehiculares con Proceso RPA**

Autor: **JJARA** | Version: **1.0.0** | Fecha: **2025-02-26**

---

## Descripcion

API REST que consulta datos vehiculares desde multiples fuentes externas (SRI, ANT, SourceDB) mediante procesos RPA en paralelo, calcula precios comerciales desde PatioTuerca y Aseguradora, y expone los resultados a traves de endpoints seguros con JWT (Keycloak).

Este proyecto es una migracion del flujo `PersonaVehiculoController` del proyecto legacy `consultas_spring` hacia una arquitectura moderna con Spring Boot 4.0.3 y Java 21.

---

## Tecnologias

| Tecnologia | Version | Proposito |
|------------|---------|-----------|
| Java | 21 (LTS) | Lenguaje |
| Spring Boot | 4.0.3 | Framework |
| Spring Data JPA | - | Persistencia |
| Spring Security + OAuth2 | - | Autenticacion JWT (Keycloak) |
| H2 Database | - | Base de datos embebida (demo) |
| SpringDoc OpenAPI | 2.8.6 | Documentacion Swagger UI |
| Lombok | - | Reduccion de boilerplate |
| Jakarta Validation | - | Validacion de inputs |
| Maven | 3.9.12 | Build tool |

---

## Arquitectura

```
ec.com.cognoware.carpriceanalytics/
├── config/                  # Seguridad, Async, Swagger, Excepciones
├── controller/              # REST endpoints (vehiculos + health)
├── dto/
│   ├── input/              # DTOs de entrada (futuro)
│   └── output/             # DTOs de salida (VehicleQueryOutput, etc.)
├── exception/              # Excepciones personalizadas
├── extractor/
│   ├── interfaces/         # Contratos para extractores RPA y precios
│   └── mock/              # Implementaciones mock para demo
├── mapper/                 # Conversion Entity <-> DTO
├── model/                  # Entidades JPA (Vehicle, VehicleModel, etc.)
├── repository/interfaces/  # Repositorios Spring Data JPA
├── service/
│   ├── interfaces/         # Contratos de servicios
│   └── impl/              # Implementaciones de negocio
└── util/                   # Constantes de aplicacion
```

---

## Flujo Principal

```
Request → Controller → VehicleQueryService
                            │
                      ┌─────┴─────┐
                      │ Cache H2  │
                      └─────┬─────┘
                       ¿Expirado?
                      /           \
                    NO             SI
                    │               │
               Retornar        Extraccion RPA
               cache           (paralelo, 25s timeout)
                                    │
                              ┌─────┼─────┐
                              SRI  SrcDB  ANT
                              (P:1) (P:2) (P:4)
                                    │
                              Merge por prioridad
                                    │
                              Homologacion modelo
                                    │
                              Calculo precios
                              (paralelo, 20s timeout)
                                    │
                              ┌─────┴─────┐
                          PatioTuerca  Aseguradora
                                    │
                              Promedio fuentes
                                    │
                              Guardar cache
                                    │
                              Response JSON
```

---

## Endpoints

### Vehiculos (requiere JWT con rol `get_pn_vehiculos`)

| Metodo | Endpoint | Descripcion |
|--------|----------|-------------|
| GET | `/{realm}/vehicles/v1/identification/{id}` | Buscar vehiculos por cedula/RUC |
| GET | `/{realm}/vehicles/v1/plate/{placa}` | Buscar vehiculo por placa |

### Publicos

| Metodo | Endpoint | Descripcion |
|--------|----------|-------------|
| GET | `/health` | Estado del servicio |
| GET | `/swagger-ui.html` | Documentacion interactiva |
| GET | `/h2-console` | Consola de base de datos H2 |
| GET | `/v3/api-docs` | OpenAPI 3 JSON |

---

## Instalacion y Ejecucion

### Requisitos

- Java 21+
- Maven 3.9+ (o usar el wrapper incluido `./mvnw`)

### Compilar

```bash
./mvnw clean compile
```

### Ejecutar tests

```bash
./mvnw test
```

### Ejecutar la aplicacion

```bash
./mvnw spring-boot:run
```

La aplicacion arranca en `http://localhost:8080/api/car-price`

### URLs de acceso

| Recurso | URL |
|---------|-----|
| Swagger UI | http://localhost:8080/api/car-price/swagger-ui.html |
| H2 Console | http://localhost:8080/api/car-price/h2-console |
| Health | http://localhost:8080/api/car-price/health |
| API Docs | http://localhost:8080/api/car-price/v3/api-docs |

### H2 Console - Credenciales

| Campo | Valor |
|-------|-------|
| JDBC URL | `jdbc:h2:mem:carpricedb` |
| User | `sa` |
| Password | *(vacio)* |

---

## Configuracion

### application.properties

| Propiedad | Valor Default | Descripcion |
|-----------|---------------|-------------|
| `server.servlet.context-path` | `/api/car-price` | Ruta base de la API |
| `spring.security.oauth2.resourceserver.jwt.issuer-uri` | `http://localhost:8080/realms/master` | URI del emisor JWT (Keycloak) |
| `extraction.timeout.default` | `25000` | Timeout de extraccion RPA (ms) |
| `extraction.timeout.price` | `20000` | Timeout de calculo de precios (ms) |
| `extraction.cache.limit-days` | `7` | Dias de vigencia del cache |
| `spring.task.execution.pool.core-size` | `5` | Hilos minimos del pool async |
| `spring.task.execution.pool.max-size` | `20` | Hilos maximos del pool async |

---

## Datos Demo Precargados

La aplicacion carga automaticamente datos de ejemplo en H2 al iniciar (`data.sql`):

| Placa | Propietario | Marca | Modelo | Anio | Precio Avg |
|-------|------------|-------|--------|------|------------|
| ABC1234 | 0102030405 | CHEVROLET | AVEO FAMILY | 2018 | $10,500 |
| XYZ5678 | 0102030405 | KIA | SPORTAGE | 2020 | $24,500 |
| DEF9012 | 0301020304 | TOYOTA | HILUX | 2019 | $32,000 |
| GHI3456 | 0704050607 | HYUNDAI | TUCSON | 2021 | $27,800 |

---

## Seguridad

La API esta protegida con **OAuth2 Resource Server** configurado para **Keycloak**:

- Los endpoints de vehiculos requieren un token JWT valido
- Los roles se extraen del claim `realm_access.roles` del token
- El rol requerido es `get_pn_vehiculos`
- Endpoints publicos: `/health`, `/swagger-ui/**`, `/h2-console/**`, `/v3/api-docs/**`

### Ejemplo de request autenticado

```bash
curl -H "Authorization: Bearer <JWT_TOKEN>" \
  http://localhost:8080/api/car-price/master/vehicles/v1/plate/ABC1234
```

---

## Extractores (Mocks)

Para la demo, los extractores RPA son **mocks** que retornan datos fijos con latencia simulada:

| Extractor | Tipo | Prioridad | Latencia | Datos |
|-----------|------|-----------|----------|-------|
| SRI | Datos vehiculares | 1 (mayor) | 2.0s | Placa, dueño, marca, modelo, VIN |
| SourceDB | Datos vehiculares | 2 | 1.5s | Placa, dueño, marca, modelo |
| ANT | Datos vehiculares | 4 (menor) | 3.0s | Placa, marca, modelo, canton |
| PatioTuerca | Precios | - | 2.5s | Avg, min, max (depreciacion 8%) |
| Aseguradora | Precios | - | 1.8s | Avg, min, max (depreciacion 7%) |

Para reemplazar un mock por la implementacion real, basta con crear una nueva clase que implemente `VehicleDataExtractor` o `VehiclePriceExtractor` y anotarla con `@Component`.

---

## Documentacion del Calculo de Precios

Ver [docs/PRICE_CALCULATION.md](docs/PRICE_CALCULATION.md) para el detalle completo de:
- Formulas de depreciacion por fuente
- Snippets de codigo con ejemplos numericos
- Diagrama del flujo de calculo
- Tabla comparativa de fuentes

---

## Mapeo desde Proyecto Legacy

| Clase Legacy (consultas_spring) | Clase Nueva (carPriceAnalytics) |
|--------------------------------|--------------------------------|
| `PersonaVehiculoController` | `VehicleQueryController` |
| `PersonaVehiculoV2Service` | `VehicleQueryServiceImpl` |
| `PersonaVehiculoV2ServiceAsync` | `VehicleExtractionServiceImpl` |
| `PersonaVehiculoTransactionalService` | `VehicleRepository` |
| `ValidadorActualizable` | `FreshnessValidator` |
| `ServicioService` | `ServiceConfigServiceImpl` |
| `VehiculoHomologacionService` | `PriceCalculationServiceImpl` |
| `PersonaVehiculoAseguradoraDto` | `ExtractedVehicleData` |
| `ConsultaPersonaVehiculo` | `VehicleQueryOutput` |
| `SRIVehiculosPythonExtractor` | `SriExtractorMock` |
| `SourceDBVehiculoExtractor` | `SourceDbExtractorMock` |
| `AntDeudaExtractor` | `AntDebtExtractorMock` |
| `PatioTuercaPrecioModeloExtractor` | `PatioTuercaPriceExtractorMock` |

---

## Estandar de Desarrollo

Este proyecto sigue el estandar definido en [estandarDesarrolloSpringboot.md](estandarDesarrolloSpringboot.md):

- Nomenclatura en **ingles** con Javadoc en **espanol**
- Estructura de paquetes: `controller/`, `service/{interfaces,impl}/`, `repository/interfaces/`, `dto/{input,output}/`, `model/`, `config/`, `exception/`, `util/`, `mapper/`
- Licencia Cognoware en todas las clases
- Javadoc con `@author`, `@version`, `@since`
- Lineas de maximo 120 caracteres
- Constantes en `UPPER_SNAKE_CASE`
- Sin entidades como DTOs en controllers
- Validacion con Jakarta Validation
- Versionado de API en URL (`/v1/`)
- Endpoint `/health` obligatorio

---

## Licencia

Copyright (c) 2025 Cognoware Cia. Ltda. Todos los derechos reservados.
