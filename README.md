# Pizza MDP - Plataforma Gastronómica Multi-Local

## 1. Overview

This document provides a comprehensive technical overview of the Pizza Management Dashboard, a full-stack application featuring a Java backend (powered by Spring Boot and Spring Security) and a React frontend. The focus of this documentation is on the security architecture, system design, and operational procedures.

The project has been refactored from a simple pizzeria model to a multi-local gastronomic platform, following Domain-Driven Design (DDD) principles.

### 1.1. Technology Stack

- **Backend:**
  - Java 21
  - Spring Boot 3.3.0
  - Spring Security (OAuth2 Authorization Server, Resource Server, OAuth2 Client)
  - Spring Data JPA
- **Frontend:**
  - React (Vite)
  - TypeScript
  - Tailwind CSS
- **Database:**
  - PostgreSQL 16
  - Flyway (Schema Versioning)
- **Messaging:**
  - RabbitMQ 3.13
- **Testing:**
  - **Backend:** JUnit 5, Mockito, Testcontainers, REST Assured
  - **Frontend:** Vitest, React Testing Library

### 1.2. Project Structure

```
.
├── PizzaMDP/           # Spring Boot Backend
│   └── src/main/java/
│       └── com/pizzamdp/
│           ├── config/         # Security & App Configuration
│           ├── controllers/    # API Endpoints
│           ├── services/       # Business Logic
│           ├── entities/       # JPA Entities (New DDD Model)
│           └── repositories/   # Data Access Layer
│
└── frontend/               # React Frontend
    └── src/
        ├── components/     # React Components
        ├── services/       # API integration
        └── App.tsx         # Main application component
```

## 2. Domain-Driven Design (DDD) - New Architecture

The data model has been redesigned to support a multi-local platform. The core concept is the **`Local` (Store)**, which acts as the primary Bounded Context for most operations.

### 2.1. Entity Relationship Diagram (Mermaid)

```mermaid
erDiagram
    Local ||--o{ Producto : "manages"
    Local ||--o{ Ingrediente : "stocks"
    Local ||--o{ Staff : "employs"
    Local ||--o{ Mesa : "has"
    Local ||--o{ Orden : "processes"

    User }|..|| Persona : "authenticates"

    Persona <|-- Cliente
    Persona <|-- Rider
    Persona <|-- Staff

    Cliente ||--o{ Orden : "places"
    Cliente }o--o{ cliente_direcciones : "has"

    Rider ||--o{ Orden : "delivers"
    Staff ||--o{ Orden : "serves as mozo"

    Producto ||--o{ Receta : "is part of"
    Ingrediente ||--o{ Receta : "is part of"
    Producto ||--o{ OrdenItem : "is sold in"

    Orden ||--o{ OrdenItem : "contains"
    Orden }|..|| Factura : "generates"
    Orden }o--|| Mesa : "is for"

    subgraph "User & Roles (Inheritance)"
        User
        Persona
        Cliente
        Rider
        Staff
    end

    subgraph "Catalog & Inventory"
        Producto
        Ingrediente
        Receta
    end

    subgraph "Operations"
        Orden
        OrdenItem
        Mesa
        Factura
    end

    subgraph "Core"
        Local
    end
```

### 2.2. Core Entities

-   **`Local`**: Represents a physical store. It's the root for catalogs, staff, and operations.
-   **`Persona` (and `User`)**: A `Persona` abstract class represents an individual, linked to a `User` for authentication. It uses `InheritanceType.JOINED` for:
    -   `Cliente`: Customers with addresses and preferences.
    -   `Rider`: Delivery personnel with vehicle and location data.
    -   `Staff`: Employees linked to a `Local` with a specific role.
-   **`Producto` / `Ingrediente` / `Receta`**: A dynamic catalog system. Each `Local` manages its own `Producto`s, which are composed of `Ingrediente`s (with stock) via a `Receta`.
-   **`Orden` / `Mesa` / `Factura`**: The operational model.
    -   `Orden` now supports types (`DELIVERY`, `SALON`, `TAKE_AWAY`) and a complex state machine.
    -   `Mesa` is used for `SALON` orders.
    -   `Factura` stores invoicing data for future integration.

## 3. Setup and Configuration

### 3.1. Prerequisites

- Docker
- Docker Compose

### 3.2. Running the Application

1.  **Clone the repository.**
2.  **Configure Google Social Login**:
    - Create OAuth 2.0 credentials in the Google Cloud Console.
    - Set the following environment variables. You can create a `.env` file in the root of the project:
      ```
      GOOGLE_CLIENT_ID=your-google-client-id
      GOOGLE_CLIENT_SECRET=your-google-client-secret
      ```
3.  **Build and run the application** using Docker Compose:
    ```bash
    sudo docker-compose up --build -d
    ```
4.  The **frontend** will be available at `http://localhost:5173`.
5.  The **backend API** will be available at `http://localhost:8080`.
6.  The **RabbitMQ Management UI** will be available at `http://localhost:15672` (user: `user`, pass: `password`).

## 4. Security Architecture

### 4.1. Role Model (Updated)

The role model is now more granular, tied to the `Persona` hierarchy.

| Role              | `Persona` Type | Description                                                                 | Authentication Method |
| ----------------- | -------------- | --------------------------------------------------------------------------- | --------------------- |
| `ROLE_ADMIN`      | `Staff`        | Can manage a `Local`'s catalog, stock, and staff.                           | Username / Password   |
| `ROLE_CLIENTE`    | `Cliente`      | A customer. Users are auto-provisioned on first login via Google.           | Google Social Login   |
| `ROLE_RIDER`      | `Rider`        | A delivery person. Can see and manage assigned orders.                      | Username / Password   |
| `ROLE_STAFF`      | `Staff`        | General staff (e.g., `MOZO`, `COCINA`). Can manage orders within their `Local`.| Username / Password   |

## 5. System Design & Flows

The asynchronous order processing flow remains the same, but the API endpoints will be adapted to the new multi-local context (e.g., endpoints might be prefixed with `/api/locales/{localId}/...`).

*(Existing diagrams for authentication and async flow are still conceptually valid and are omitted for brevity).*

## 6. API Endpoint Guide (To Be Updated)

**Note**: The API endpoints are pending a refactor to align with the new multi-local, resource-oriented model. For example:

-   `GET /api/locales/{localId}/productos`
-   `POST /api/locales/{localId}/ordenes`

This section will be updated once the controllers are refactored.

## 7. Environment Variables

| Variable             | Description                                                                                             | Example                                    |
| -------------------- | ------------------------------------------------------------------------------------------------------- | ------------------------------------------ |
| `GOOGLE_CLIENT_ID`     | The OAuth 2.0 Client ID obtained from the Google Cloud Console for social login.                        | `your-google-client-id.apps.googleusercontent.com` |
| `GOOGLE_CLIENT_SECRET` | The client secret associated with the Google OAuth 2.0 Client ID.                                       | `GOCSPX-your-secret`                       |
| `DB_PASSWORD`        | The password for the PostgreSQL database user. This is automatically used by Docker Compose.            | `password`                                 |
| `CORS_ALLOWED_ORIGIN`| The origin allowed to make cross-origin requests to the backend. Defaults to `http://localhost:5173`. | `http://your-frontend-domain.com`          |
