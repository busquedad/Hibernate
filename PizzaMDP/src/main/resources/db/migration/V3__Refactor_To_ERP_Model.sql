-- V3: Refactor To ERP Model for Multi-Local Platform
-- This is a destructive migration. It removes old pizza-centric tables
-- and creates a new, normalized schema based on DDD principles.

-- Step 1: Drop old tables.
-- We drop them in reverse order of dependency.
-- Foreign key constraints are dropped automatically with the tables.
DROP TABLE IF EXISTS orden_item;
DROP TABLE IF EXISTS orden;
DROP TABLE IF EXISTS variedadpizza;
DROP TABLE IF EXISTS tipopizza;
DROP TABLE IF EXISTS tamaniopizza;

-- Step 2: Create new core and catalog tables.

-- Represents a physical store location.
CREATE TABLE locales (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    direccion VARCHAR(255) NOT NULL,
    latitud NUMERIC(10, 7),
    longitud NUMERIC(10, 7),
    descripcion TEXT,
    estado VARCHAR(50) NOT NULL -- ABIERTO, CERRADO
);

-- Represents a sellable product at a specific local.
CREATE TABLE productos (
    id BIGSERIAL PRIMARY KEY,
    local_id BIGINT NOT NULL,
    nombre VARCHAR(255) NOT NULL,
    descripcion TEXT,
    precio_base NUMERIC(10, 2) NOT NULL,
    categoria VARCHAR(100),
    CONSTRAINT fk_producto_local FOREIGN KEY(local_id) REFERENCES locales(id)
);

-- Represents an ingredient with stock at a specific local.
CREATE TABLE ingredientes (
    id BIGSERIAL PRIMARY KEY,
    local_id BIGINT NOT NULL,
    nombre VARCHAR(255) NOT NULL,
    unidad_medida VARCHAR(50), -- e.g., 'grams', 'units'
    stock_actual INT NOT NULL,
    CONSTRAINT fk_ingrediente_local FOREIGN KEY(local_id) REFERENCES locales(id)
);

-- Links productos to the ingredientes required to make them.
CREATE TABLE recetas (
    id BIGSERIAL PRIMARY KEY,
    producto_id BIGINT NOT NULL,
    ingrediente_id BIGINT NOT NULL,
    cantidad INT NOT NULL, -- Amount of ingredient for one unit of the product
    CONSTRAINT fk_receta_producto FOREIGN KEY(producto_id) REFERENCES productos(id),
    CONSTRAINT fk_receta_ingrediente FOREIGN KEY(ingrediente_id) REFERENCES ingredientes(id)
);

-- Step 3: Create user hierarchy tables (based on InheritanceType.JOINED).

-- Abstract base table for all persons. Links to the auth 'users' table.
CREATE TABLE personas (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT UNIQUE,
    nombre VARCHAR(255),
    apellido VARCHAR(255),
    telefono VARCHAR(50),
    CONSTRAINT fk_persona_user FOREIGN KEY(user_id) REFERENCES users(id)
);

-- Concrete table for Customers.
CREATE TABLE clientes (
    id BIGINT PRIMARY KEY,
    preferencias TEXT,
    CONSTRAINT fk_cliente_persona FOREIGN KEY(id) REFERENCES personas(id)
);

-- Stores customer addresses.
CREATE TABLE cliente_direcciones (
    cliente_id BIGINT NOT NULL,
    direccion VARCHAR(255) NOT NULL,
    CONSTRAINT fk_direccion_cliente FOREIGN KEY(cliente_id) REFERENCES clientes(id)
);

-- Concrete table for Riders.
CREATE TABLE riders (
    id BIGINT PRIMARY KEY,
    vehiculo VARCHAR(100),
    patente VARCHAR(50),
    latitud_actual NUMERIC(10, 7),
    longitud_actual NUMERIC(10, 7),
    estado VARCHAR(50) NOT NULL, -- DISPONIBLE, OCUPADO, INACTIVO
    CONSTRAINT fk_rider_persona FOREIGN KEY(id) REFERENCES personas(id)
);

-- Concrete table for Staff members.
CREATE TABLE staff (
    id BIGINT PRIMARY KEY,
    local_id BIGINT NOT NULL,
    rol_operativo VARCHAR(50), -- MOZO, COCINA, DESPACHANTE, ADMIN
    CONSTRAINT fk_staff_persona FOREIGN KEY(id) REFERENCES personas(id),
    CONSTRAINT fk_staff_local FOREIGN KEY(local_id) REFERENCES locales(id)
);


-- Step 4: Create new operational tables.

-- Represents a dining table in a local.
CREATE TABLE mesas (
    id BIGSERIAL PRIMARY KEY,
    local_id BIGINT NOT NULL,
    numero VARCHAR(50) NOT NULL,
    capacidad INT,
    sector VARCHAR(100),
    estado VARCHAR(50) NOT NULL, -- DISPONIBLE, OCUPADA, RESERVADA
    CONSTRAINT fk_mesa_local FOREIGN KEY(local_id) REFERENCES locales(id)
);

-- The new, refactored Order table.
CREATE TABLE ordenes (
    id BIGSERIAL PRIMARY KEY,
    local_id BIGINT NOT NULL,
    cliente_id BIGINT NOT NULL,
    fecha_orden TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    tipo_orden VARCHAR(50) NOT NULL,   -- DELIVERY, TAKE_AWAY, SALON
    estado_orden VARCHAR(50) NOT NULL, -- PENDIENTE, EN_COCINA, etc.
    -- For SALON orders
    mesa_id BIGINT,
    mozo_id BIGINT,
    -- For DELIVERY orders
    rider_id BIGINT,
    direccion_entrega TEXT,
    CONSTRAINT fk_orden_local FOREIGN KEY(local_id) REFERENCES locales(id),
    CONSTRAINT fk_orden_cliente FOREIGN KEY(cliente_id) REFERENCES clientes(id),
    CONSTRAINT fk_orden_mesa FOREIGN KEY(mesa_id) REFERENCES mesas(id),
    CONSTRAINT fk_orden_mozo FOREIGN KEY(mozo_id) REFERENCES staff(id),
    CONSTRAINT fk_orden_rider FOREIGN KEY(rider_id) REFERENCES riders(id)
);

-- Line items for an order.
CREATE TABLE orden_items (
    id BIGSERIAL PRIMARY KEY,
    orden_id BIGINT NOT NULL,
    producto_id BIGINT NOT NULL,
    cantidad INT NOT NULL,
    precio_unitario NUMERIC(10, 2) NOT NULL,
    CONSTRAINT fk_item_orden FOREIGN KEY(orden_id) REFERENCES ordenes(id),
    CONSTRAINT fk_item_producto FOREIGN KEY(producto_id) REFERENCES productos(id)
);

-- Stores invoice data for future integration.
CREATE TABLE facturas (
    id BIGSERIAL PRIMARY KEY,
    orden_id BIGINT NOT NULL UNIQUE,
    cae VARCHAR(255),
    fecha_vencimiento_cae DATE,
    fecha_factura DATE,
    total NUMERIC(10, 2),
    tipo_factura VARCHAR(10), -- A, B, C
    prefijo VARCHAR(20),
    numero_factura VARCHAR(100),
    url_pdf TEXT,
    estado VARCHAR(50), -- PENDIENTE, GENERADA, CANCELADA, ERROR
    CONSTRAINT fk_factura_orden FOREIGN KEY(orden_id) REFERENCES ordenes(id)
);
