# Calculo de Precios Vehiculares

**Autor:** JJARA
**Version:** 1.0.0
**Fecha:** 2025-02-26

---

## Descripcion General

El sistema calcula el precio comercial de un vehiculo consultando **multiples proveedores de precios habilitados en paralelo**, promediando los resultados y aplicando una **homologacion de modelos** para mapear el modelo tecnico (ej. `AVEO FAMILY`) al modelo comercial (ej. `AVEO FAMILY 1.5L`).

---

## Flujo Completo del Calculo

```
┌─────────────────────────────────────────────────────────┐
│                   REQUEST (placa/cedula)                 │
└──────────────────────────┬──────────────────────────────┘
                           │
                           ▼
              ┌────────────────────────┐
              │   1. BUSCAR EN CACHE   │
              │    (VehicleRepository)  │
              └────────────┬───────────┘
                           │
                    ┌──────┴──────┐
                    │ ¿Expirado?  │
                    └──────┬──────┘
                     NO/   │   \SI
                    /      │     \
                   ▼       │      ▼
            Retornar       │   ┌──────────────────────────┐
            cache          │   │  2. CONSULTA PARALELA    │
                           │   │  (CompletableFuture)     │
                           │   │  Timeout: 25 segundos    │
                           │   └──────────┬───────────────┘
                           │              │
                           │    ┌─────────┼──────────┐
                           │    ▼         ▼          ▼
                           │  ┌────┐  ┌──────┐  ┌──────┐
                           │  │Prov│  │Prov  │  │Prov  │
                           │  │Pri │  │Sec   │  │Terc  │
                           │  │P:1 │  │P:2   │  │P:4   │
                           │  │2s  │  │1.5s  │  │3s    │
                           │  └────┘  └──────┘  └──────┘
                           │              │
                           │              ▼
                           │   ┌──────────────────────────┐
                           │   │  3. MERGE POR PRIORIDAD  │
                           │   │  Primario(1) >            │
                           │   │  Secundario(2) >          │
                           │   │  Terciario(4)             │
                           │   └──────────┬───────────────┘
                           │              │
                           │              ▼
                           │   ┌──────────────────────────┐
                           │   │  4. HOMOLOGACION          │
                           │   │  AVEO → AVEO FAMILY 1.5L │
                           │   └──────────┬───────────────┘
                           │              │
                           │              ▼
                           │   ┌──────────────────────────┐
                           │   │  5. CALCULO DE PRECIOS    │
                           │   │  (CompletableFuture)      │
                           │   │  Timeout: 20 segundos     │
                           │   └──────────┬───────────────┘
                           │              │
                           │    ┌─────────┴──────────┐
                           │    ▼                    ▼
                           │ ┌──────────┐    ┌──────────┐
                           │ │Proveedor │    │Proveedor │
                           │ │Precios A │    │Precios B │
                           │ │  2.5s    │    │  1.8s    │
                           │ └──────────┘    └──────────┘
                           │         │            │
                           │         ▼            ▼
                           │   ┌──────────────────────────┐
                           │   │  6. PROMEDIO DE FUENTES   │
                           │   │  avg = Σ(avg) / N         │
                           │   │  min = Σ(min) / N         │
                           │   │  max = Σ(max) / N         │
                           │   └──────────┬───────────────┘
                           │              │
                           │              ▼
                           │   ┌──────────────────────────┐
                           │   │  7. GUARDAR EN CACHE      │
                           │   │  (VehicleRepository.save) │
                           │   └──────────┬───────────────┘
                           │              │
                           └──────────────┘
                                   │
                                   ▼
                          ┌─────────────────┐
                          │   RESPONSE JSON  │
                          └─────────────────┘
```

---

## Snippet: Calculo de Depreciacion por Fuente

Cada proveedor de precios aplica su propia formula de depreciacion. A continuacion el snippet de la logica utilizada:

### Proveedor de Precios A

```java
/**
 * Formula de depreciacion Proveedor A:
 *
 *   precioBase      = $12,500.00 (valor base de referencia)
 *   tasaDepreciacion = 8% anual
 *   aniosUso        = anioReferencia - anioVehiculo
 *
 *   depreciacion = tasaDepreciacion * aniosUso
 *   factor       = 1 - depreciacion  (minimo 0.20 = 20% del valor)
 *   precioFinal  = precioBase * factor
 *
 *   precioMinimo  = precioFinal * 0.85  (-15%)
 *   precioMaximo  = precioFinal * 1.15  (+15%)
 *
 * Ejemplo: CHEVROLET AVEO 2018 (7 anios de uso)
 *   depreciacion = 0.08 * 7 = 0.56
 *   factor       = 1 - 0.56 = 0.44
 *   precioFinal  = 12500 * 0.44 = $5,500.00
 *   precioMinimo = 5500 * 0.85  = $4,675.00
 *   precioMaximo = 5500 * 1.15  = $6,325.00
 */
private BigDecimal calculateMockPrice(Vehicle vehicle) {
    int yearsOld = REFERENCE_YEAR
        - (vehicle.getVehicleYear() != null
            ? vehicle.getVehicleYear() : REFERENCE_YEAR);
    if (yearsOld < 0) {
        yearsOld = 0;
    }
    // Depreciacion lineal: 8% por anio
    BigDecimal depreciation = DEPRECIATION_RATE.multiply(
        new BigDecimal(yearsOld)
    );
    // Factor minimo: 20% (vehiculo nunca vale menos del 20%)
    BigDecimal factor = BigDecimal.ONE.subtract(depreciation);
    if (factor.compareTo(new BigDecimal("0.20")) < 0) {
        factor = new BigDecimal("0.20");
    }
    return BASE_PRICE.multiply(factor);
}
```

### Proveedor de Precios B

```java
/**
 * Formula de depreciacion Proveedor B:
 *
 *   valorAsegurado   = $13,000.00 (valor base asegurado)
 *   tasaDepreciacion  = 7% anual
 *   aniosUso         = anioReferencia - anioVehiculo
 *
 *   depreciacion = tasaDepreciacion * aniosUso
 *   factor       = 1 - depreciacion  (minimo 0.25 = 25% del valor)
 *   precioFinal  = valorAsegurado * factor
 *
 *   precioMinimo  = precioFinal * 0.90  (-10%)
 *   precioMaximo  = precioFinal * 1.10  (+10%)
 *
 * Ejemplo: CHEVROLET AVEO 2018 (7 anios de uso)
 *   depreciacion = 0.07 * 7 = 0.49
 *   factor       = 1 - 0.49 = 0.51
 *   precioFinal  = 13000 * 0.51 = $6,630.00
 *   precioMinimo = 6630 * 0.90  = $5,967.00
 *   precioMaximo = 6630 * 1.10  = $7,293.00
 */
private BigDecimal calculateMockPrice(Vehicle vehicle) {
    int yearsOld = REFERENCE_YEAR
        - (vehicle.getVehicleYear() != null
            ? vehicle.getVehicleYear() : REFERENCE_YEAR);
    if (yearsOld < 0) {
        yearsOld = 0;
    }
    // Depreciacion lineal: 7% por anio
    BigDecimal depreciation = YEARLY_DEPRECIATION.multiply(
        new BigDecimal(yearsOld)
    );
    // Factor minimo: 25% (piso de valor asegurado)
    BigDecimal factor = BigDecimal.ONE.subtract(depreciation);
    if (factor.compareTo(new BigDecimal("0.25")) < 0) {
        factor = new BigDecimal("0.25");
    }
    return BASE_INSURED_VALUE.multiply(factor);
}
```

---

## Snippet: Promedio de Multiples Fuentes

Una vez obtenidos los precios de cada proveedor, se promedian:

```java
/**
 * Promedio de precios de N proveedores:
 *
 *   precioPromedio = Σ(avgPrice[i]) / N
 *   precioMinimo   = Σ(minPrice[i]) / N
 *   precioMaximo   = Σ(maxPrice[i]) / N
 *
 * Ejemplo con 2 proveedores (Proveedor A + Proveedor B):
 *   avgProveedorA = $5,500.00   avgProveedorB = $6,630.00
 *   minProveedorA = $4,675.00   minProveedorB = $5,967.00
 *   maxProveedorA = $6,325.00   maxProveedorB = $7,293.00
 *
 *   precioPromedio = (5500 + 6630) / 2 = $6,065.00
 *   precioMinimo   = (4675 + 5967) / 2 = $5,321.00
 *   precioMaximo   = (6325 + 7293) / 2 = $6,809.00
 */
private void applyAveragePrices(
    Vehicle vehicle,
    List<PriceDetailOutput> prices
) {
    BigDecimal avgSum = BigDecimal.ZERO;
    BigDecimal minSum = BigDecimal.ZERO;
    BigDecimal maxSum = BigDecimal.ZERO;
    int count = prices.size();
    List<String> sources = new ArrayList<>();

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
}
```

---

## Snippet: Homologacion de Modelos

Antes de calcular precios, se homologa el modelo tecnico al comercial:

```java
/**
 * Homologacion:
 *   Busca en tabla vehicle_homologation por marca + modelo.
 *   Asigna el modelo comercial al vehiculo.
 *
 * Ejemplo:
 *   INPUT:  brand="CHEVROLET", model="AVEO"
 *   TABLA:  brand="CHEVROLET", model="AVEO" → commercial_model="AVEO FAMILY 1.5L"
 *   OUTPUT: vehicle.commercialModel = "AVEO FAMILY 1.5L"
 */
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
    }
}
```

---

## Snippet: Consulta Paralela a Proveedores con CompletableFuture

La consulta a proveedores de datos y precios se ejecuta en paralelo con timeout:

```java
/**
 * Consulta paralela a proveedores:
 *   1. Lanza N extractores en hilos separados (@Async)
 *   2. Espera a que todos completen (o timeout de 25s)
 *   3. Recolecta resultados exitosos
 *   4. Ordena por prioridad (1=Primario, 2=Secundario, 4=Terciario)
 *   5. Merge: el primer campo no-nulo gana (mayor prioridad)
 */
List<CompletableFuture<ExtractedVehicleData>> futures =
    extractors.stream()
        .map(ext -> ext.extractByPlate(licensePlate)
            .exceptionally(ex -> {
                log.warn("Extractor {} fallo: {}",
                    ext.getSourceName(), ex.getMessage());
                return null;  // No falla el flujo
            }))
        .toList();

// Esperar con timeout configurable
CompletableFuture<Void> allFutures =
    CompletableFuture.allOf(
        futures.toArray(new CompletableFuture[0])
    );
allFutures.get(extractionTimeoutMs, TimeUnit.MILLISECONDS);

// Recolectar y ordenar por prioridad
List<ExtractedVehicleData> results = futures.stream()
    .map(f -> f.getNow(null))
    .filter(Objects::nonNull)
    .sorted(Comparator.comparingInt(
        ExtractedVehicleData::getOrderPriority
    ))
    .toList();
```

---

## Tabla Comparativa de Proveedores de Precios

| Proveedor | Tipo | Depreciacion | Piso | Rango | Latencia (mock) |
|-----------|------|-------------|------|-------|-----------------|
| Proveedor de Precios A | Proveedor habilitado | 8% anual | 20% | +/- 15% | 2.5s |
| Proveedor de Precios B | Proveedor habilitado | 7% anual | 25% | +/- 10% | 1.8s |

---

## Ejemplo de Respuesta JSON

```json
{
  "vehicles": [
    {
      "licensePlate": "ABC1234",
      "ownerIdentification": "0102030405",
      "ownerName": "JUAN CARLOS PEREZ LOPEZ",
      "brand": "CHEVROLET",
      "model": "AVEO FAMILY",
      "vehicleYear": 2018,
      "color": "BLANCO",
      "engineDisplacement": "1500",
      "vehicleClass": "AUTOMOVIL",
      "serviceType": "PARTICULAR",
      "vin": "9GASP48X09B123456",
      "canton": "CUENCA",
      "country": "ECUADOR",
      "commercialModel": "AVEO FAMILY 1.5L",
      "priceDetail": {
        "averagePrice": 6065.00,
        "minPrice": 5321.00,
        "maxPrice": 6809.00,
        "source": "PRICE_PROVIDER_A,PRICE_PROVIDER_B",
        "calculationDate": "2025-02-26T10:30:00"
      }
    }
  ],
  "message": null
}
```

---

## Archivos Relacionados

| Archivo | Descripcion |
|---------|-------------|
| `service/impl/PriceCalculationServiceImpl.java` | Orquestador de calculo de precios |
| `service/impl/VehicleExtractionServiceImpl.java` | Orquestador de consulta paralela a proveedores |
| `service/impl/VehicleQueryServiceImpl.java` | Orquestador principal del flujo completo |
| `extractor/mock/PriceProviderAExtractorMock.java` | Mock de precios Proveedor A |
| `extractor/mock/PriceProviderBExtractorMock.java` | Mock de precios Proveedor B |
| `extractor/interfaces/VehiclePriceExtractor.java` | Interfaz contrato para extractores de precios |
| `model/VehicleHomologation.java` | Entidad de homologacion marca-modelo |
| `mapper/VehicleMapper.java` | Merge de datos extraidos por prioridad |
