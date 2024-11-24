-- triggers.sql
-- Crear tabla única de auditoría
CREATE TABLE IF NOT EXISTS public.auditoria
(
    id_auditoria SERIAL PRIMARY KEY,        -- ID único para cada registro de auditoría
    tabla VARCHAR(255) NOT NULL,            -- Nombre de la tabla donde ocurrió el cambio
    operacion VARCHAR(10) NOT NULL,         -- Tipo de operación: 'INSERT', 'UPDATE', 'DELETE'
    consulta TEXT,                          -- Descripción detallada de la operación
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- Fecha y hora exacta de la operación
    )
    TABLESPACE pg_default;

-- Establecer el propietario de la tabla
ALTER TABLE IF EXISTS public.auditoria
    OWNER TO postgres;

-- Función genérica para auditoría
CREATE OR REPLACE FUNCTION registrar_auditoria()
RETURNS TRIGGER AS $$
DECLARE
operacion_texto TEXT;
BEGIN
    -- Construir la descripción de la operación según el tipo de acción
    IF TG_OP = 'INSERT' THEN
        operacion_texto := 'INSERT realizado. Datos nuevos: ' || row_to_json(NEW)::TEXT;
    ELSIF TG_OP = 'UPDATE' THEN
        operacion_texto := 'UPDATE realizado. Datos anteriores: ' || row_to_json(OLD)::TEXT ||
                           '. Datos nuevos: ' || row_to_json(NEW)::TEXT;
    ELSIF TG_OP = 'DELETE' THEN
        operacion_texto := 'DELETE realizado. Datos eliminados: ' || row_to_json(OLD)::TEXT;
END IF;

    -- Insertar registro en la tabla de auditoría
INSERT INTO auditoria (tabla, operacion, consulta)
VALUES (TG_TABLE_NAME, TG_OP, operacion_texto);

-- Retornar el registro adecuado
IF TG_OP = 'DELETE' THEN
        RETURN OLD;
ELSE
        RETURN NEW;
END IF;
END;
$$ LANGUAGE plpgsql;

-- Crear triggers para todas las tablas relevantes
-- Tabla: clientes
CREATE TRIGGER trigger_auditoria_clientes
    AFTER INSERT OR UPDATE OR DELETE ON clientes
    FOR EACH ROW
    EXECUTE FUNCTION registrar_auditoria();

-- Tabla: categorias
CREATE TRIGGER trigger_auditoria_categorias
    AFTER INSERT OR UPDATE OR DELETE ON categorias
    FOR EACH ROW
    EXECUTE FUNCTION registrar_auditoria();

-- Tabla: productos
CREATE TRIGGER trigger_auditoria_productos
    AFTER INSERT OR UPDATE OR DELETE ON productos
    FOR EACH ROW
    EXECUTE FUNCTION registrar_auditoria();

-- Tabla: ordenes
CREATE TRIGGER trigger_auditoria_ordenes
    AFTER INSERT OR UPDATE OR DELETE ON ordenes
    FOR EACH ROW
    EXECUTE FUNCTION registrar_auditoria();

CREATE TRIGGER trigger_auditoria_detalleordenes
    AFTER INSERT OR UPDATE OR DELETE ON detalleordenes
    FOR EACH ROW
    EXECUTE FUNCTION registrar_auditoria();
