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
