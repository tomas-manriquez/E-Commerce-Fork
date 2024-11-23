-- createDB.sql
-- Table: public.clientes

-- Eliminar la tabla si ya existe (para evitar errores)
-- DROP TABLE IF EXISTS public.clientes;

-- Crear la tabla clientes
CREATE TABLE IF NOT EXISTS public.clientes
(
    idcliente BIGSERIAL PRIMARY KEY,  -- ID autoincremental
    nombre character varying(255),     -- Nombre del cliente
    direccion character varying(255),  -- Dirección del cliente
    email character varying(255),      -- Email del cliente
    telefono character varying(20),    -- Teléfono del cliente
    password character varying(255),   -- Contraseña encriptada del cliente
    username character varying(255),   -- Nombre de usuario
    rol character varying(50)          -- Rol del cliente (como ADMIN, USER, etc.)
    )
    TABLESPACE pg_default;

-- Establecer el propietario de la tabla
ALTER TABLE IF EXISTS public.clientes
    OWNER TO postgres;

-- Table: public.categorias

-- Eliminar la tabla si ya existe (para evitar errores)
-- DROP TABLE IF EXISTS public.categorias;

-- Crear la tabla categorias
CREATE TABLE IF NOT EXISTS public.categorias
(
    idcategoria BIGSERIAL PRIMARY KEY,  -- ID autoincremental
    nombre character varying(255)       -- Nombre de categoría
    )
    TABLESPACE pg_default;

-- Establecer el propietario de la tabla
ALTER TABLE IF EXISTS public.categorias
    OWNER TO postgres;

-- Table: public.productos

-- Eliminar la tabla si ya existe (para evitar errores)
-- DROP TABLE IF EXISTS public.productos;

-- Crear la tabla productos
CREATE TABLE IF NOT EXISTS public.productos
(
    idproducto BIGSERIAL PRIMARY KEY,   -- ID autoincremental
    nombre character varying(255),      -- Nombre del producto
    descripcion text,                   -- Descripción del producto
    precio DECIMAL(10,2),               -- Precio del producto
    stock int,                          -- Cantidad disponible del producto
    estado character varying(50),       -- Estado del producto ("disponible", "agotado")
    idcategoria BIGINT,                 -- Categoría del producto
    CONSTRAINT fk_categoria FOREIGN KEY (idcategoria) REFERENCES public.categorias (idcategoria)
    )
    TABLESPACE pg_default;

-- Establecer el propietario de la tabla
ALTER TABLE IF EXISTS public.productos
    OWNER TO postgres;

-- Table: public.ordenes

-- Eliminar la tabla si ya existe (para evitar errores)
-- DROP TABLE IF EXISTS public.ordenes;

-- Crear la tabla ordenes
CREATE TABLE IF NOT EXISTS public.ordenes
(
    idorden BIGSERIAL PRIMARY KEY,      -- ID autoincremental
    fechaorden TIMESTAMP,               -- Fecha en que se realizó la orden
    estado character varying(50),       -- Estado de la orden ("pendiente","pagada","enviada")
    idcliente BIGINT,                   -- Cliente que realizó la orden
    total DECIMAL(10,2),                -- Total a pagar por la orden
    CONSTRAINT fk_cliente FOREIGN KEY (idcliente) REFERENCES public.clientes (idcliente)
    )
    TABLESPACE pg_default;

-- Establecer el propietario de la tabla
ALTER TABLE IF EXISTS public.ordenes
    OWNER TO postgres;


-- Table: public.detalleordenes

-- Eliminar la tabla si ya existe (para evitar errores)
-- DROP TABLE IF EXISTS public.detalleordenes;

-- Crear la tabla detalleordenes
CREATE TABLE IF NOT EXISTS public.detalleordenes
(
    iddetalle BIGSERIAL PRIMARY KEY,        -- ID autoincremental
    idorden BIGINT,                         -- Referencia a la orden
    idproducto BIGINT,                      -- Referencia al producto
    cantidad int,                           -- Cantidad de producto en la orden
    preciounitario DECIMAL(10,2),            -- Precio unitario del producto
    CONSTRAINT fk_orden FOREIGN KEY (idorden) REFERENCES public.ordenes (idorden),
    CONSTRAINT fk_producto FOREIGN KEY (idproducto) REFERENCES public.productos (idproducto)
    )
    TABLESPACE pg_default;

-- Establecer el propietario de la tabla
ALTER TABLE IF EXISTS public.detalleordenes
    OWNER TO postgres;

/*
 -- Table: public.detalleordenes

-- DROP TABLE IF EXISTS public.detalleordenes;

CREATE TABLE IF NOT EXISTS public.detalleordenes
(
    iddetalle bigint NOT NULL DEFAULT nextval('detalleordenes_iddetalle_seq'::regclass),
    idorden bigint,
    idproducto bigint,
    cantidad integer,
    preciounitario numeric(10,2),
    CONSTRAINT detalleordenes_pkey PRIMARY KEY (iddetalle),
    CONSTRAINT fk_orden FOREIGN KEY (idorden)
        REFERENCES public.ordenes (idorden) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk_producto FOREIGN KEY (idproducto)
        REFERENCES public.productos (idproducto) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.detalleordenes
    OWNER to postgres;
 */