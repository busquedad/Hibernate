-- V2: Add User Management, Social Login, and Rider Assignment

-- 1. Create the users table for authentication and user data
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE, -- Corresponds to email
    password VARCHAR(255),                 -- Nullable for Google users
    provider VARCHAR(50) NOT NULL          -- e.g., 'LOCAL', 'GOOGLE'
);

-- 2. Create a table to manage user roles
CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    role VARCHAR(255) NOT NULL,
    CONSTRAINT fk_user_roles_user FOREIGN KEY(user_id) REFERENCES users(id),
    PRIMARY KEY (user_id, role)
);

-- 3. Alter the 'orden' table to link to the new 'users' table
-- First, add the new columns that will become foreign keys
ALTER TABLE orden ADD COLUMN cliente_id BIGINT;
ALTER TABLE orden ADD COLUMN rider_id BIGINT;

-- Note: In a real-world scenario with existing data, we'd need a strategy
-- to populate 'cliente_id' for existing orders before making it NOT NULL.
-- Since this is likely a fresh schema, we proceed directly.

-- Now, drop the old, denormalized columns
ALTER TABLE orden DROP COLUMN nombre_cliente;
ALTER TABLE orden DROP COLUMN direccion_cliente;

-- Finally, make the cliente_id column non-nullable and add foreign key constraints
ALTER TABLE orden ALTER COLUMN cliente_id SET NOT NULL;
ALTER TABLE orden ADD CONSTRAINT fk_orden_cliente FOREIGN KEY(cliente_id) REFERENCES users(id);
ALTER TABLE orden ADD CONSTRAINT fk_orden_rider FOREIGN KEY(rider_id) REFERENCES users(id);
