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
