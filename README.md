# Pizza Management Dashboard

This is a full-stack application that allows users to view pizza sizes. It consists of a Java backend powered by Spark and Hibernate, and a React frontend built with Vite.

## Project Structure

- `PizzaMDP/`: The Java backend.
- `frontend/`: The React frontend.
- `Database/`: Database scripts and configuration.
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

## API Usage

### GET /api/pizzas

Returns a list of all available pizza sizes.

**Example Request:**

```bash
curl http://localhost:8080/api/pizzas
```

**Example Response:**

```json
[
  {
    "id_tamanio_pizza": 1,
    "nombre": "Chica",
    "cant_porciones": 4
  },
  {
    "id_tamanio_pizza": 2,
    "nombre": "Grande",
    "cant_porciones": 8
  }
]
```
