# ![Cognoware Cia Ltda | Cuenca](https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSwpTqVK5816amcbXoAKGNBbyeK0N7Bxv_nyA&s)Estándar de Desarrollo

# **Estándar de desarrollo proyectos JAVA**

### Versión

- Número: 1.0.0
- Fecha: 25/11/2024



| AUTOR      | FECHA      | ACCIONES              | OBSERVACIONES |
| ---------- | ---------- | --------------------- | ------------- |
| JJARA | 25/11/2025 | Creación de documento | -             |
| JJARA      | 25/11/2025 | Aprobación            | -             |
|            |            |                       |               |



## Visión general

El propósito es seguir lineamientos de buenas practicas de desarrollo en proyectos desarrollados por la empresa.

## Spring boot

1. Nombre de paquetes:

   1. ```controller``` → Contiene los controladores `REST` que exponen los endpoints de la API.
   2. `service` → Contiene la lógica de negocio y la gestión de transacciones.
      1. `impl`: Implementación de interfaces
      2. `interfaces`: Definición de interfaces

   3. `repository` → Interfaces que gestionan la persistencia de datos mediante `JPA`, `Hibernate` e `integraciones externas`
      1. `impl`  
      2. `interfaces`

   4. `dto` → Objetos de transferencia de datos utilizados para estructurar las respuestas y solicitudes.
      1. `input`:
         1. DTO usados unicamente en el ingreso de datos a través de un servicio REST.
      2. `interfaces`  : Definicion de estructuras base y repetibles como:
         1. nonce,
         2. OtpToken
      3. `output` : DTO que serán expuestos mediante el endpoint, no se puede usar para ingresar datos
      4. `projections`: Usamos para mapear de forma automatica SQL personalizados al objeto 
   5. `security` → Implementación de la seguridad basada en `JWT` y `Spring Security.`
   6. `util` → Clases utilitarias y herramientas comunes del sistema.
   7. `config` → Contiene configuraciones generales como seguridad, `CORS` y manejadores globales de excepciones, majeno de cache
   8. `exception` → Módulo de manejo de excepciones centralizadas y personalizadas.
   9. `aspect` → Implementaciones de `AOP` para auditoría y seguridad.
   10. `model` → Entidades JPA que representan las tablas de la base de datos.mapper → Clases que convierten entidades a DTO y viceversa.

### 1. Cumplimiento de estándar de código estático

- Se debe cumplir con el perfil de calidad asignado al proyecto `Quality Profiles` 

- En nuevos desarrollos se debe llegar a las métricas de calidad establecidas de **"A"**: 

- En desarrollos con anterioridad, es decir, desarrollos existentes, se debe minimizar los **issues** por cada **push**


### 2. Construcción de una API REST:

#### Principios Generales

- Una API debe ser **clara, consistente y predecible** en su estructura y funcionamiento.
- Debe seguir el principio **SRP (Single Responsibility Principle)**: cada API debe realizar **una única función específica** y no mezclar responsabilidades.

#### **Detalle**

-  **Responsabilidad única**:
  - Una API no debe tener múltiples funcionalidades que no correspondan a su propósito definido.
  - Ejemplo: Una API cuyo propósito es listar (`GET`) no debe modificar, eliminar o agregar registros.
-  **Segregación de funcionalidades**:
  - Cada API debe estar especializada en una única acción.
  - Ejemplo para un mantenimiento de catálogo de países:
    -  API para **crear** un país → `POST /createCountries`
    -  API para **actualizar** un país → `POST /updateCountries`
    -  API para **eliminar** un país → `POST /deleteCountries`
    -  API para **consultar** países → `GET /getCountriesByCode`
    -  API para **consultar por ID** → `GET /getCountriesById`
  - Los métodos HTTPS usados serán:
    - GET
    - POST

------

### **Restricciones y Reglas**

#### **1. Métodos HTTP Correctos**

-  `GET` → Para obtener información. **Nunca debe modificar datos.**
-  `POST` → Para crear un nuevo recurso o para obtener información o para crear un nuevo recurso

#### **2. Eliminación de Datos**

-  **PELIGRO**: No debe existir eliminación física a menos que sea estrictamente necesario y solicitado por el cliente.
  - En su lugar, considerar un **estado de inactividad** (`activo: 1 y eliminado: 9`).  
  - En caso de requerir eliminación fisica es obligatorio: documentar y justificars en el código mediante javadoc

#### **3. Versionado de API**

-  Toda API debe incluir **versionado en la URL**:
  - Ejemplo: `https://api.miempresa.com/countries/v1/deleteCountry`
  - Se debe incrementar el número de versión cuando haya **cambios incompatibles** con versiones anteriores.

#### **4. Seguridad**

- No se debe enviar datos sensibles por `RequestParam`
- Se debe realizar la `Sanitizaciónizar` los datos de entrada

#### **6. Estandarización de Respuestas**

-  Todas las respuestas deben seguir un formato JSON estructurado.
-  En caso de error, la API debe devolver **códigos HTTP apropiados**:
  - `200 OK` → Respuesta exitosa.
  - `201 Created` → Recurso creado exitosamente.
  - `400 Bad Request` → Error en los parámetros enviados.
  - `401 Unauthorized` → No autenticado.
  - `403 Forbidden` → No autorizado.
  - `404 Not Found` → Recurso no encontrado.
  - `500 Internal Server Error` → Error inesperado del servidor.

#### **7. Documentación**

-  Todo controlador, tanto DTO de entrada y de salida debe estar documentado basado en el estandar explicado en los puntos posteriores con JAVADOC
-  Se debe registrar en Postman, los nuevos request, con la siguiente organización:
   -  [PROYECTO]
      -  [sbucarpeta]
         -  [PATH de API]. Ejemplo: /countries/v1/deleteCountry
         -  [PATH de API]. Ejemplo: /countries/v1/createCountry




### 3. Documentación de código:

#### Documentación:

-  Licencia por cada clase:

  ````java
  /**
   * {{NOMBRE_SISTEMA}}, {{DESCRIPCION DE SISTEMA}} 
   * Copyright (c) 2025 Cognoware Cia. Ltda.
   * Todos los derechos reservados.
   * <p>
   * Este archivo forma parte del {{NOMBRE_SISTEMA}} de
   * Cognoware Cia. Ltda, desarrollado conforme a los requerimientos y
   * normativas establecidos.
   */
  package ec.fin.jardinazuayo.sive.service.impl;
  ````

-  Documentación de clase: 

  ```java
  
  /**
   * {{QUE}}{{COMO}}{{PARA QUE}} -> MAX 80 caracteres, explicación corta
   *
   * {{DETALLE DEL DESARROLLO}}  -> SIN LIMITE DE TEXTO, OPCIONAL
   *
   * @author {{INICIALNOMBRE + APELLIDO}}
   * @version {{VERSION DE DOCUMENTO, INICIA EN 1.0.0}}
   * @since {{YYYY-MM-DD Del Desarrollo}}
   */
  @Service
  public class EncryptionServiceImpl 
  ```

  

-  Documentación de cada método:

  ```java
      /**
       * {{QUE}}{{COMO}} y/o {{PARA QUE}} -> MAX 80 caracteres, explicación breve
       *
       * {{Detalle en caso de un nivel de complejidad alto o Detalle de REQ.}}  -> SIN LIMITE DE TEXTO, OPCIONAL
       * @param input {{QUE}}{{PARA QUE}} -> MAX 50 Caracteres
       * @return {{Resultado de metodo}} -> MAX 50 caracteres.
       */
      @PostMapping("/encrypt")
      public String encryptData(@RequestBody InputDecode input) {
          return encryptionService.encrypt(sanitizeInputData(input));
      }
  ```

  En caso de hereder de una interfaz, no se documenta la implementación:

  ````java
      /**
       * {@inheritDoc}
       */
      public String encryptObject(InputDecode input) {
  ````

-  Documentación de constantes:

  ```java
      /**
       * {{QUE ES}}-> MAX 80 caracteres
       * Mensaje de error de "se requiere clave"
       */
      public static final String MSG_KEY_IS_REQUIRED = "MSG_KEY_IS_REQUIRED";
  
  
  ```


### 4. Formato  de código

1. Cada linea de código no debe sobrepasar los 120 caracteres de largo, en caso de sobrepasar se debe escribir en la siguiente linea el código:

   - Incorrecto

     ````java
     GenericResponse<EmployeeVacationPlanningDto> saveEmployeeVacationPlanning(EmployeeVacationPlanningDto planningDto, Boolean ignoreWarning, Boolean statusId, Boolean idEmployee ) throws GenericException, ValidationException;
         ....
     
     ````

     

   -  Correcto

     ```java
     GenericResponse<EmployeeVacationPlanningDto> saveEmployeeVacationPlanning(
         EmployeeVacationPlanningDto planningDto, 
         Boolean ignoreWarning, 
         Boolean statusId, 
         Boolean idEmployee ) throws GenericException, 
     								ValidationException;
     
     ```

     

### 5. Nombrar **clases**, **métodos**, **variables**, **constantes**, **paquetes** e **interfaces**.

| Elemento       | Estilo                            | Ejemplo                             | **Prefijos Recomendados**                                    |
| -------------- | --------------------------------- | ----------------------------------- | ------------------------------------------------------------ |
| **Clases**     | `PascalCase`                      | `CustomerOrder`, `UserAccount`      | Uso de prefijo según su paquete: `AdministratorController`, `EmailServiceImpl`, `MapperElectoralRegisterType` |
| **Interfaces** | `PascalCase`                      | `Printable`, `DataParser`           | `EmailService`                                               |
| **Métodos**    | `camelCase`                       | `getUserName()`, `calculateTotal()` | Uso de prefijo según su fin: `get`, `set`, `is`, `calculate`, `find`, `update`, `delete`, `create`, `get, find, search, list, fetch, query, count, is, exists, check, validate, read, has, load` |
| **Variables**  | `camelCase`                       | `orderTotal`, `userName`            | Prefijos según contexto: `is` (boolean)                      |
| **Constantes** | `UPPER_SNAKE_CASE`                | `MAX_VALUE`, `PI_VALUE`             | Prefijos comunes: `MAX, MIN, DEFAULT, APP, CONFIG,` `MSG_`   |
| **Paquetes**   | `lowercase`                       | `ec.com.cognoware.project`          | Prefijos organizacionales: `com`, `org`, `net`, `io`, `com.ec` |
| **Enums**      | `PascalCase` y `UPPER_SNAKE_CASE` | `OrderStatus`, `PENDING`            | Sin prefijos en el nombre del Enum. Valores en `UPPER_SNAKE_CASE`. |
| **Parámetros** | `camelCase`                       | `orderId`, `customerName`           | Prefijos según  según contexto                               |



Restricciones:

- Un nombre  no debe superar un umbral de 50 caracteres.



### 6. Consideraciones de seguridad

#### **1. Prevención de Inyección SQL**

> La sanitización consiste en remover los caracteres especiales de los datos de entrada. No permitir valores no válidos según el contexto del parámetro de entrada.

####  **Mala Práctica: Vulnerable a SQL Injection**

```java
@Query("SELECT u FROM User u WHERE u.username = '" + nombre + "'")

```

#### **Buena Práctica: Usar Parámetros**

```java
@Query("SELECT u FROM User u WHERE u.username = :nombre")
User findByUsername(@Param("nombre") String nombre);

```

#### **2. Sanitización**  de datos de entrada en controladores

-  Usar `jakarta.validation` en DTOs

  ```java
  import jakarta.validation.constraints.NotBlank;
  import jakarta.validation.constraints.Size;
  
  @Getter
  @Setter
  @NoArgsConstructor
  @JsonInclude(JsonInclude.Include.NON_NULL)
  @JsonIgnoreProperties(ignoreUnknown = true)
  @EqualsAndHashCode
  public class UserRequestDTO implements Serializable {
  
      @Serial
      private static final long serialVersionUID = 1L;
  
      @NotBlank(message = "El nombre no puede estar vacío")
      @Size(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")
      private String nombre;
  
  }
  
  ```

  Aplicando a controller:

  ```java
  import jakarta.validation.Valid;
  import org.springframework.http.ResponseEntity;
  import org.springframework.web.bind.annotation.*;
  
  @RestController
  @RequestMapping("/users")
  public class UserController {
  
      // @Valid Para activar los validares
      @PostMapping
      public ResponseEntity<String> createUser(@Valid @RequestBody UserRequestDTO userRequest) {
          return ResponseEntity.ok("Usuario creado: " + userRequest.getNombre());
      }
  }
  
  ```

  

- Usando métodos genéricos para validar

  ```java
  @PostMapping("/getUsers")
  public ResponseEntity<String> getUsers(@RequestParam String input) {
      String sanitizedInput = SanitizationUtil.sanitizeInput(input);
      //... more code
      return ResponseEntity.ok(sanitizedInput);
  }
  
  ```

  

- A través de clases abstractas

  Implementación de DTO:

  ```java
  // DTO que implementa los datos a controlar
  public class InputDecode implements Serializable, SanitizableData {
  
      @Serial
      private static final long serialVersionUID = 1L;
  
      private String data;
      private InputDataDTO inputDataDTO;
  
  }
  
  // INTERFAZ PARA INTEGRAR
  public interface SanitizableData {
  
      /**
       * Obtiene el dato almacenado en la implementación de la interfaz.
       *
       * @return el dato en su estado actual.
       * @since 2025-01-14
       */
      String getData();
  
      /**
       * Establece un nuevo valor para el dato, permitiendo su sanitización antes de ser procesado.
       *
       * @param data el nuevo dato a almacenar.
       * @since 2025-01-14
       */
      void setData(String data);
  }
  
  ```

  Definición de lógica de depurado de datos

  ```java
  /**
   * Clase abstracta que define el método para sanitizar los datos de entrada.
   * La sanitización consiste en remover los caracteres especiales de los datos de entrada.
   *
   * @author Cognoware
   * @version 1.0
   * @since 2025-01-15
   */
  public class SanitizationAbstract {
  
      /**
       * Sanitizes the input data to remove special characters.
       *
       * @param input An object implementing SanitizableData with data to sanitize.
       * @return The sanitized object.
       * @throws IllegalArgumentException If the input data is null or empty.
       *
       * @since 2025-01-15
       */
      public static <T extends SanitizableData> T sanitizeInputData(T input) {
          if (input == null || input.getData() == null || input.getData().isEmpty()) {
              throw new IllegalArgumentException("Invalid input: data cannot be null or empty");
          }
          // Sanitize the input data by removing special characters
          String sanitizedData = input.getData().replaceAll(
              ConstantsApp.REGEX_FORBIDDEN_CHARACTERS, "_");
          input.setData(sanitizedData);
          return input;
      }
  }
  
  ```

  Implementación final:

  ````java
  @RestController
  @RequestMapping("/encryption")
  public class EncryptionController extends SanitizationAbstract {
  
      private final EncryptionService encryptionService;
  
      @Autowired
      public EncryptionController(EncryptionService encryptionService) {
          this.encryptionService = encryptionService;
      }
  
      /**
       * Encripta una cadena de datos.
       *
       * @param input El objeto de entrada que contiene la cadena a encriptar.
       * @return La cadena encriptada.
       */
      @PostMapping("/encrypt")
      public String encryptData(@RequestBody InputDecode input) {
          return encryptionService.encrypt(sanitizeInputData(input));
      }
  }
      
  ````

  

### 7. Documentación de servicio web en Postman

Cada servicio web desarrollado debe contar con una colección en **Postman** que documente sus endpoints y sea exportada en formato `.json`. Esto garantizará que cada desarrollo incluya pruebas organizadas y reutilizables.



**Estructura de la Colección**

- `[Nombre de proyecto]`: Nombre del proyecto. En formato `UPPER_SNAKE_CASE``

**Servicios web organizados en carpetas según su fin**

> Carpetas por funcionalidad: Agrupar endpoints en carpetas dentro de la colección creada

- `[Nombre de carpeta]`: Nombre de la carpeta. En formato `PascalCase``

- `[Descripción de carpeta]:` Detalle de la carpeta, debe responder a las preguntas ¿Qué es?  ¿Para qué? o ¿Por qué?

**Organización de los Endpoints**

> Nombres descriptivos en cada endpoint

- `[Nombre de enpoint] [EndPoint]`:Nombres descriptivos + url de `endPoint`

  

  

### 8. Calidad de código

- En proyectos nuevos la nomenclatura de codigo sera en ingles, la documentacion de javadoc en español
  - No se permite nombres en español-ingles
  - Los nombres propios si se mantiene en Español. Ejemplo: Función Judicial del Ecuador, Super intendencia de .. SUPERCIAS., Si se mantiene en mayuculas nombres propios
  - En poryectos nuevos se debe implemntar la libreria para control de exepciones personalizadas :
    - https://mvnrepository.com/artifact/ec.com.cognoware/common-handler
  - En proyectos existente  como consultas, novascoring: el estandar sera en español
- El estandar de base de datos ser de acuerdo al estandar definido en archivo de Estandar de base de datos.
- Las entidades seran mapeadas en ingles, expecto los campons que hagan relacion a un nombre propio.
- El uso de try-catch debe estar justificado, no se debe usar a expeción que el desarrollo lo amerite en ese caso justificar con javadoc
- Todo proyecto debe implemntar la controller clase /health bsaado en el estandar obligado common-handler 
- No se puede usar una entidad ni como entrada ni salida de un controller
- No se puede usar una entidad como DTO durante la logica de negocio
- Todo valor fijo debe estar declarado en una costante.
- En caso de requerir constantes de tipo catalogo se debe usar ENUMS
- **Principios SOLID**: identificar violaciones claras (ej. SRP, OCP)  
- **API REST**: verbos correctos (GET, POST, PUT, DELETE), versionado, códigos HTTP adecuado
- No debe existir parametros quemados en codigo ni en UTILS. Ejemplo timpo en milesgundos de conexion: debe estar en properties o base de datos
- Propiedades tomadas de base de datos deben estar cachadas, no se permite el acceso recurente a consultas de base de datos de parametros estaticos
- **SQL inconsistente o ambiguo**
  - Consultas que devuelven múltiples filas cuando se espera una sola.
  - Queries sin `ORDER BY` cuando el orden es crítico.
- BATH deben temer control de:
  - Multiples nodos de ejecucion
  - Bloqueos
  - Se deben poder detener
  - Reiniciar
  - Reproesar
  - 





OTROS:

- **API REST**: verbos correctos (GET, POST, PUT, DELETE), versionado, códigos HTTP adecuado

​	
