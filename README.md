# Pizza Management Dashboard

This is a full-stack application that allows users to manage pizzas. It consists of a Java backend powered by Spring Boot and Spring Security, and a React frontend built with Vite.

## Project Structure

- `PizzaMDP/`: The Java backend.
- `frontend/`: The React frontend.
- `docker-compose.yml`: Docker Compose file for running the application.

## Getting Started

### Prerequisites

- Docker
- Docker Compose

### Running the Application

1. Clone the repository.
2. Run `docker-compose up --build` to build and start the application.
3. The frontend will be available at `http://localhost:5173`.
4. The backend API will be available at `http://localhost:8080`.

## Security Configuration

The application is secured using OAuth 2.0 and OpenID Connect, with a Spring Authorization Server and a Resource Server. It features a Role-Based Access Control (RBAC) system with two predefined roles:

- **`ADMINISTRADOR`**: Full access to all API endpoints.
- **`USUARIO`**: Limited access to specific endpoints.

### Test Users

- **Username**: `admin`, **Password**: `password`, **Role**: `ADMINISTRADOR`
- **Username**: `user`, **Password**: `password`, **Role**: `USUARIO`

### Protected Endpoints

| Endpoint                  | `ADMINISTRADOR` | `USUARIO` |
| ------------------------- | --------------- | --------- |
| `/oms/ordenes`            | ✅               | ✅         |
| `/oms/ordenes/status`     | ✅               | ✅         |
| `/oms/**`                 | ✅               | ❌         |
| `/catalogo/**`            | ✅               | ❌         |
| `/stock/**`               | ✅               | ❌         |
| `/api/me`                 | ✅               | ✅         |

### Authentication Flow

The application uses the OAuth 2.0 Authorization Code Grant with PKCE.

```mermaid
graph TD
    A[Frontend] -->|1. Redirect to /oauth2/authorize| B(Authorization Server)
    B -->|2. User Login| B
    B -->|3. Redirect with Authorization Code| A
    A -->|4. Exchange Code for Token at /oauth2/token| B
    B -->|5. Return JWT Access Token| A
    A -->|6. Access Protected API with Token| C(Resource Server)
    C -->|7. Validate Token and Roles| C
    C -->|8. Return Protected Resource| A
```

## API Usage

### GET /api/me

Returns information about the authenticated user, including their username and roles.

**Example Request:**

```bash
curl -H "Authorization: Bearer <YOUR_ACCESS_TOKEN>" http://localhost:8080/api/me
```

**Example Response:**

```json
{
  "username": "admin",
  "roles": ["ADMINISTRADOR"]
}
```

### Other Endpoints

All other API endpoints under `/api/**` are protected and require a valid JWT access token.
