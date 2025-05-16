# MotoShop – Convenciones de código y estructura

## Paquetes

```
ec.puce.motoshop.{layer}
```

* `domain`       – entidades JPA  
* `repository`   – interfaces `JpaRepository`  
* `service`      – lógica (`I*Service` + `*ServiceImpl`)  
* `dto`          – objetos de transferencia  
* `controller`   – `@RestController`  
* `integration`  – clientes REST externos (banca, Amazon)  
* `config`       – configuración global de Spring (seguridad, Swagger, etc.)

---

## Herramientas clave

| Propósito         | Librería                |
|-------------------|-------------------------|
| Mapeo DTO         | MapStruct 1.5.x         |
| Documentación     | springdoc-openapi       |
| Gestión de BD     | Spring Data JPA         |
| Seguridad futura  | Spring Security + JWT   |
| Pruebas           | JUnit 5 + Testcontainers + WireMock |

---

## Perfiles de ejecución

* `dev` – PostgreSQL o H2 local, configuración simplificada  
* `prod` – PostgreSQL nube, con integración real

---

## Reglas FK (`ON DELETE`)

| Relación                       | Estrategia         |
|-------------------------------|--------------------|
| Relaciones débiles            | `SET NULL`         |
| Composición (detalles, hijos) | `CASCADE`          |
| Catálogos / referencias duras | `RESTRICT`         |

---

## Estilo de endpoints REST

```
GET    /api/productos             → Obtener todos
GET    /api/productos/{id}        → Obtener por ID
POST   /api/productos             → Crear nuevo
PUT    /api/productos/{id}        → Actualizar por ID
DELETE /api/productos/{id}        → Eliminar por ID
```

→ Se aplica de forma similar para `/api/clientes`, `/api/categorias`, etc.

---

## Estructura interna por módulo

```
src/
 └── main/
     ├── java/
     │    └── ec/puce/motoshop/[modulo]
     │         ├── controller/
     │         ├── service/
     │         ├── dto/
     │         ├── domain/
     │         └── repository/
     └── resources/
```

> Las carpetas `dto`, `repository` o `controller` pueden omitirse si el módulo no las requiere.

---

## Convenciones de nombres

- **DTOs**: `EntidadDTO` → `ProductoDTO`, `ClienteDTO`
- **Interfaces de servicio**: `ProductoService`
- **Implementaciones**: `ProductoServiceImpl`
- **Controladores REST**: `ProductoController`
- **Clientes externos (REST)**: `BancaWebClient`, `AmazonCoreClient`

---

## Pruebas por módulo

| Módulo              | Tipo de prueba       | Herramientas         |
|---------------------|----------------------|-----------------------|
| `domain`            | Modelo, entidades    | JPA + Testcontainers |
| `service`           | Lógica + mock        | JUnit + Mockito      |
| `api`               | Controladores        | @WebMvcTest          |
| `integration`       | Clientes REST        | WireMock             |
| `config`            | Seguridad, Swagger   | SpringBootTest       |

---

## Seguridad inicial

Durante la primera fase:

- La autenticación se hace enviando el número de cédula como header HTTP:

```
X-Cedula: 0102345678
```

- En fases posteriores se migrará a **JWT**, validado contra la **API de Banca Web**.

---

## Documentación y monitoreo

- Documentación automática con Swagger/OpenAPI (`springdoc-openapi`)
- Salud del sistema con `spring-boot-starter-actuator`
- Pruebas automatizadas vía Postman (Colecciones exportadas)

---

## Integración con APIs externas

Los clientes externos estarán ubicados en el módulo `motorshop-integration` y gestionarán:

- **Banca Web**: autenticación, saldo, pagos
- **Amazon Core**: productos sincronizados, stock, compra
- **Orquestador**: registro de eventos y validación de integraciones