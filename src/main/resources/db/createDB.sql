-- createDB.sql

-- Verifica si la base de datos existe. Si no existe, la crea.
DO
$$
    BEGIN
        IF NOT EXISTS (SELECT 1 FROM pg_database WHERE datname = 'ecommerce') THEN
            EXECUTE 'CREATE DATABASE ecommerce';
        END IF;
    END
$$;

-- Conectar a la base de datos
\connect ecommerce

-- Table: public.clientes

-- Eliminar la tabla si ya existe (para evitar errores)
-- DROP TABLE IF EXISTS public.clientes;

-- Habilitar la extensión PostGIS
CREATE EXTENSION IF NOT EXISTS postgis;

-- Crear la tabla tienda (necesaria para los productos y repartidores)
CREATE TABLE IF NOT EXISTS public.tienda (
    idtienda BIGSERIAL PRIMARY KEY,     -- ID autoincremental
    nombre VARCHAR(255) NOT NULL        -- Nombre de la tienda
);

-- Crear la tabla clientes
CREATE TABLE IF NOT EXISTS public.clientes (
    idcliente BIGSERIAL PRIMARY KEY,   -- ID autoincremental
    nombre character varying(255),     -- Nombre del cliente
    direccion character varying(255),  -- Dirección del cliente
    email character varying(255),      -- Email del cliente
    telefono character varying(20),    -- Teléfono del cliente
    password character varying(255),   -- Contraseña encriptada del cliente
    username character varying(255),   -- Nombre de usuario
    rol character varying(50)          -- Rol del cliente (como ADMIN, USER, etc.)
    );

-- Crear la tabla categorias
CREATE TABLE IF NOT EXISTS public.categorias (
    idcategoria BIGSERIAL PRIMARY KEY,  -- ID autoincremental
    nombre character varying(255)       -- Nombre de categoría
    );

-- Crear la tabla productos con la relación a categorias
CREATE TABLE IF NOT EXISTS public.productos (
    idproducto BIGSERIAL PRIMARY KEY,   -- ID autoincremental
    nombre character varying(255),      -- Nombre del producto
    descripcion text,                   -- Descripción del producto
    precio DECIMAL(10,2),               -- Precio del producto
    stock int,                          -- Cantidad disponible del producto
    estado character varying(50),       -- Estado del producto ("disponible", "agotado")
    idcategoria BIGINT,                 -- Categoría del producto
    idtienda BIGINT,                       -- Relación con la tienda
    CONSTRAINT fk_categoria FOREIGN KEY (idcategoria) REFERENCES public.categorias (idcategoria),
    CONSTRAINT fk_tienda FOREIGN KEY (idtienda) REFERENCES public.tienda (idtienda)
    );

-- Crear la tabla repartidor con relación a tienda
CREATE TABLE IF NOT EXISTS public.repartidores (
    idrepartidor BIGSERIAL PRIMARY KEY, -- ID autoincremental
    nombre VARCHAR(255) NOT NULL,       -- Nombre del repartidor
    apellido VARCHAR(255) NOT NULL,     -- Apellido del repartidor
    idtienda INT REFERENCES public.tienda (idtienda) -- Relación con la tienda
    );

-- Crear la tabla ordenes con relación a clientes
CREATE TABLE IF NOT EXISTS public.ordenes (
    idorden BIGSERIAL PRIMARY KEY,      -- ID autoincremental
    fechaorden TIMESTAMP,               -- Fecha en que se realizó la orden
    estado character varying(50),       -- Estado de la orden ("pendiente","pagada","enviada")
    idcliente BIGINT,                   -- Cliente que realizó la orden
    total DECIMAL(10,2),                -- Total a pagar por la orden
    identrega BIGINT,
    CONSTRAINT fk_cliente FOREIGN KEY (idcliente) REFERENCES public.clientes (idcliente) ON DELETE SET NULL
    CONSTRAINT fk_entrega FOREIGN KEY (identrega) REFERENCES public.entregas (identrega) ON DELETE SET NULL

    );

-- Crear la tabla detalleordenes con relaciones a ordenes y productos
CREATE TABLE IF NOT EXISTS public.detalleordenes (
    iddetalle BIGSERIAL PRIMARY KEY,    -- ID autoincremental
    idorden BIGINT,                     -- Referencia a la orden
    idproducto BIGINT,                  -- Referencia al producto
    cantidad int,                       -- Cantidad de producto en la orden
    preciounitario DECIMAL(10,2),       -- Precio unitario del producto
    CONSTRAINT fk_orden FOREIGN KEY (idorden) REFERENCES public.ordenes (idorden),
    CONSTRAINT fk_producto FOREIGN KEY (idproducto) REFERENCES public.productos (idproducto) ON DELETE SET NULL
    );

-- Crear la tabla zona con geometría
CREATE TABLE IF NOT EXISTS public.zona (
    idzona BIGSERIAL PRIMARY KEY,         -- Identificador único de la zona
    idtienda BIGINT REFERENCES public.tienda (idtienda), -- Relación opcional con la tienda
    nombrezona VARCHAR(255) NOT NULL,  -- Nombre de la zona
    geom GEOMETRY(POLYGON, 0)          -- Geometría de la zona (SRID 4326)
    );

-- Crear la tabla ordenes con relación a clientes
CREATE TABLE IF NOT EXISTS public.entregas (
    identrega BIGSERIAL PRIMARY KEY,      -- ID autoincremental
    idrepartidor BIGINT,
    idorden BIGINT,
    lugarentrega GEOMETRY(POINT, 0),    -- Punto de entrega (coordenadas geográficas)
    fechaentrega TIMESTAMP,               -- Fecha en que se realizó la orden
    CONSTRAINT fk_repartidor FOREIGN KEY (idrepartidor) REFERENCES public.repartidores (idrepartidor) ON DELETE SET NULL
    CONSTRAINT fk_orden FOREIGN KEY (idorden) REFERENCES public.ordenes (idorden) ON DELETE SET NULL
    );