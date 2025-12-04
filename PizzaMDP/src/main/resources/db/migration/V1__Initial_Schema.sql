CREATE TABLE tamaniopizza (
    id_tamanio_pizza SERIAL PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    cant_porciones INT NOT NULL
);

CREATE TABLE tipopizza (
    id_tipo_pizza SERIAL PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    descripcion VARCHAR(255)
);

CREATE TABLE variedadpizza (
    id_variedad_pizza SERIAL PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    ingredientes VARCHAR(255)
);

CREATE TABLE orden (
    id_orden SERIAL PRIMARY KEY,
    nombre_cliente VARCHAR(255) NOT NULL,
    direccion_cliente VARCHAR(255) NOT NULL,
    fecha_orden TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    estado_orden VARCHAR(50) NOT NULL
);

CREATE TABLE orden_item (
    id_orden_item SERIAL PRIMARY KEY,
    id_orden INT NOT NULL,
    id_variedad_pizza INT NOT NULL,
    id_tamanio_pizza INT NOT NULL,
    id_tipo_pizza INT NOT NULL,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(10, 2) NOT NULL,
    CONSTRAINT fk_orden FOREIGN KEY(id_orden) REFERENCES orden(id_orden),
    CONSTRAINT fk_variedad_pizza FOREIGN KEY(id_variedad_pizza) REFERENCES variedadpizza(id_variedad_pizza),
    CONSTRAINT fk_tamanio_pizza FOREIGN KEY(id_tamanio_pizza) REFERENCES tamaniopizza(id_tamanio_pizza),
    CONSTRAINT fk_tipo_pizza FOREIGN KEY(id_tipo_pizza) REFERENCES tipopizza(id_tipo_pizza)
);
