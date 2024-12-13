-- triggers.sql adaptado

-- Crear tabla única de auditoría
CREATE TABLE IF NOT EXISTS public.auditoria
(
    id_auditoria SERIAL PRIMARY KEY,        -- ID único para cada registro de auditoría
    tabla VARCHAR(255) NOT NULL,            -- Nombre de la tabla donde ocurrió el cambio
    operacion VARCHAR(10) NOT NULL,         -- Tipo de operación: 'INSERT', 'UPDATE', 'DELETE'
    consulta TEXT,                          -- Descripción detallada de la operación
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Fecha y hora exacta de la operación
    user_id BIGINT                          -- ID del usuario que realizó la operación
    );

-- Crear la tabla sesion
CREATE TABLE IF NOT EXISTS public.sesion
(
    user_id BIGINT NOT NULL DEFAULT 0 -- Contendrá el ID del usuario conectado
);

-- Insertar valor inicial en la tabla sesion
INSERT INTO public.sesion (user_id)
VALUES (0)
    ON CONFLICT DO NOTHING; -- Evita errores si la tabla ya tiene un valor

-- Modificar la función de auditoría para obtener el user_id desde la tabla sesion
CREATE OR REPLACE FUNCTION registrar_auditoria()
RETURNS TRIGGER AS $$
DECLARE
operacion_texto TEXT;
    usuario_actual BIGINT;
BEGIN
    -- Obtener el user_id desde la tabla sesion
SELECT user_id INTO usuario_actual FROM public.sesion LIMIT 1;

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
INSERT INTO auditoria (tabla, operacion, consulta, user_id)
VALUES (TG_TABLE_NAME, TG_OP, operacion_texto, usuario_actual);

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

-- Tabla: detalleordenes
CREATE TRIGGER trigger_auditoria_detalleordenes
    AFTER INSERT OR UPDATE OR DELETE ON detalleordenes
    FOR EACH ROW
    EXECUTE FUNCTION registrar_auditoria();

-- Tabla: tiendas
CREATE TRIGGER trigger_auditoria_tiendas
    AFTER INSERT OR UPDATE OR DELETE ON tiendas
    FOR EACH ROW
    EXECUTE FUNCTION registrar_auditoria();

-- Tabla: repartidor
CREATE TRIGGER trigger_auditoria_repartidor
    AFTER INSERT OR UPDATE OR DELETE ON repartidores
    FOR EACH ROW
    EXECUTE FUNCTION registrar_auditoria();

-- Tabla: zona
CREATE TRIGGER trigger_auditoria_zona
    AFTER INSERT OR UPDATE OR DELETE ON zonas
    FOR EACH ROW
    EXECUTE FUNCTION registrar_auditoria();
