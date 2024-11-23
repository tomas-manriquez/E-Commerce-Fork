-- auditProductos.sql

-- Tabla de auditoria para cambios en tabla de Productos
CREATE TABLE IF NOT EXISTS public.auditoria_productos
(
    idauditoria SERIAL PRIMARY KEY,
    idcliente BIGINT NOT NULL,
    idproducto BIGINT NOT NULL,
    operacion VARCHAR(10) NOT NULL, -- 'INSERT', 'UPDATE', 'DELETE'
    consulta TEXT,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_cliente FOREIGN KEY (idcliente) REFERENCES public.clientes (idcliente) ON DELETE CASCADE,
    CONSTRAINT fk_producto FOREIGN KEY (idproducto) REFERENCES public.productos (idproducto) ON DELETE CASCADE
    )
    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.auditoria_productos
    OWNER TO postgres;

-- Función para auditoría de INSERT
CREATE OR REPLACE FUNCTION registrar_auditoria_insert()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO auditoria_productos (idcliente, idproducto, operacion, consulta)
    VALUES (
                NEW.idcliente,
                NEW.idproducto,
                'INSERT',
                'Producto nuevo: ' ||
                'Nombre=' || NEW.nombre || '; ' ||
                'Precio=' || NEW.precio || '; ' ||
                'Stock=' || NEW.stock
           );
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;


-- Función para auditoría de UPDATE
CREATE OR REPLACE FUNCTION registrar_auditoria_update()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO auditoria_productos (idcliente, idproducto, operacion, consulta)
    VALUES (
               NEW.idcliente,
               NEW.idproducto,
               'UPDATE',
               'Producto actualizado: ' ||
               'Nombre: Antes=' || OLD.nombre || ', Después=' || NEW.nombre || '; ' ||
               'Precio: Antes=' || OLD.precio || ', Después=' || NEW.precio || '; ' ||
               'Stock: Antes=' || OLD.stock || ', Después=' || NEW.stock
           );
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;



-- Función para auditoría de DELETE
CREATE OR REPLACE FUNCTION registrar_auditoria_delete()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO auditoria_productos (idcliente, idproducto, operacion, consulta)
    VALUES (
                OLD.idcliente,
                OLD.idproducto,
                'DELETE',
                'Producto eliminado: Nombre=' || NEW.nombre
           );
    RETURN OLD;
END;
$$ LANGUAGE plpgsql;


-- Crear triggers
CREATE TRIGGER trigger_auditoria_insert
    AFTER INSERT ON productos
    FOR EACH ROW
    EXECUTE FUNCTION registrar_auditoria_insert();

CREATE TRIGGER trigger_auditoria_update
    AFTER UPDATE ON productos
    FOR EACH ROW
    EXECUTE FUNCTION registrar_auditoria_update();

CREATE TRIGGER trigger_auditoria_delete
    AFTER DELETE ON productos
    FOR EACH ROW
    EXECUTE FUNCTION registrar_auditoria_delete();