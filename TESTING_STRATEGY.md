# Estrategia de Pruebas

Este documento describe la estrategia de pruebas automatizadas para el backend de PizzaMDP.

## Tipos de Pruebas

La suite de pruebas está diseñada para cubrir diferentes niveles de la aplicación, garantizando la calidad, estabilidad y seguridad del código.

### 1. Pruebas Unitarias

- **Objetivo:** Verificar la lógica de negocio de los componentes de forma aislada.
- **Frameworks:** JUnit 5, Mockito.
- **Ubicación:** `src/test/java/com/pizzamdp/services/`
- **Descripción:** Estas pruebas se centran en las clases de servicio (`CatalogService`, `OrdersService`). Las dependencias de la capa de persistencia (repositorios) son simuladas (mocked) con Mockito para asegurar que solo se prueba la lógica de negocio.

### 2. Pruebas de Integración

- **Objetivo:** Validar la interacción entre los diferentes componentes de la aplicación, incluyendo la API, la capa de servicio y la base de datos.
- **Frameworks:** Spring Boot Test, JUnit 5, Testcontainers, REST Assured.
- **Ubicación:** `src/test/java/com/pizzamdp/integration/`
- **Descripción:** Estas pruebas levantan un contexto completo de Spring Boot y se conectan a una base de datos PostgreSQL real, gestionada por Testcontainers. Se utilizan para:
    - Verificar que las migraciones de Flyway se aplican correctamente.
    - Probar los flujos completos de la API (ej. crear y consultar una orden).
    - Asegurar que la configuración de la aplicación es correcta.

### 3. Pruebas de Seguridad

- **Objetivo:** Garantizar que los mecanismos de seguridad funcionan como se espera.
- **Frameworks:** Spring Boot Test, Spring Security Test, REST Assured.
- **Ubicación:** `src/test/java/com/pizzamdp/integration/SecurityTest.java`
- **Descripción:** Estas pruebas verifican que los endpoints protegidos no son accesibles sin la autenticación y autorización adecuadas. Se comprueban los siguientes escenarios:
    - Acceso a un endpoint protegido sin un token (esperando un 401 Unauthorized).
    - Acceso a un endpoint protegido con un token válido pero con roles insuficientes (esperando un 403 Forbidden).

### 4. Pruebas de Carga (Smoke Test)

- **Objetivo:** Detectar problemas de rendimiento y latencia en la conexión con la base de datos.
- **Frameworks:** JUnit 5, REST Assured.
- **Ubicación:** `src/test/java/com/pizzamdp/integration/OrdersIntegrationTest.java`
- **Descripción:** Se ha implementado una prueba de carga simple que crea un número significativo de órdenes (100) en un bucle. La prueba falla si la operación excede un umbral de tiempo predefinido, lo que podría indicar un problema de rendimiento.

## Cómo Ejecutar las Pruebas

Para ejecutar la suite de pruebas completa, navegue al directorio del backend (`PizzaMDP`) y ejecute el siguiente comando de Maven:

```bash
mvn test
```

Esto ejecutará todas las pruebas (unitarias, de integración, de seguridad y de carga) y generará un informe de los resultados.

### 5. Pruebas de Frontend

- **Objetivo:** Garantizar la calidad y estabilidad de la interfaz de usuario de React.
- **Frameworks:** Vitest, React Testing Library, jsdom.
- **Ubicación:** `frontend/src/**/*.test.tsx`
- **Descripción:** Las pruebas de frontend se centran en verificar que los componentes de React se renderizan correctamente y se comportan como se espera en un entorno de navegador simulado.
    - **Smoke Tests:** Se utilizan para asegurar que los componentes principales (como `App.tsx`) se montan sin errores.
    - **Component Tests:** Pruebas más específicas que verifican la presencia de elementos clave en la interfaz de usuario (ej. campos de formulario, botones).

#### Cómo Ejecutar las Pruebas de Frontend

Para ejecutar la suite de pruebas de frontend, navegue al directorio `frontend` y ejecute el siguiente comando:

```bash
pnpm test
```

Esto ejecutará todas las pruebas de componentes y generará un informe de los resultados, incluyendo la cobertura de código.
