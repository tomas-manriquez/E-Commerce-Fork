-- storedProcedures.sql

-- Procedimiento ID 17 Reporte de usuarios con más queries
CREATE OR REPLACE PROCEDURE reporte_auditoria()
LANGUAGE plpgsql
AS $$
DECLARE
    record_audit RECORD;
BEGIN
    -- Iterar sobre los resultados de la consulta
    FOR record_audit IN
        SELECT user_id, operacion, COUNT(*) AS cantidad
        FROM auditoria
        GROUP BY user_id, operacion
        ORDER BY cantidad DESC
    LOOP
        -- Mostrar los resultados en la consola
        RAISE NOTICE 'Usuario: %, Operación: %, Cantidad: %',
                     record_audit.user_id,
                     record_audit.operacion,
                     record_audit.cantidad;
    END LOOP;
END;
$$;

-- Procedimiento ID 18
CREATE OR REPLACE PROCEDURE registrar_orden(
    p_idcliente BIGINT,
    p_estado VARCHAR(50),
    p_productos JSON
)
LANGUAGE plpgsql
AS $$
DECLARE
    v_idorden BIGINT; -- Variable para almacenar el ID de la nueva orden
    v_producto RECORD; -- Variable para iterar sobre los productos
    v_preciounitario DECIMAL(10,2); -- Precio unitario del producto
BEGIN
    -- Crear la orden inicialmente con un total de 0
    INSERT INTO ordenes (fechaorden, estado, idcliente, total)
    VALUES (CURRENT_TIMESTAMP, p_estado, p_idcliente, 0)
    RETURNING idorden INTO v_idorden;

-- Iterar sobre el JSON de productos
    FOR v_producto IN SELECT * FROM json_to_recordset(p_productos) AS (
            idproducto BIGINT,
            cantidad INT
        ) LOOP
        -- Obtener el precio unitario del producto
        SELECT precio INTO v_preciounitario
        FROM productos
        WHERE idproducto = v_producto.idproducto;

        -- Insertar el detalle de la orden
        INSERT INTO detalleordenes (idorden, idproducto, cantidad, preciounitario)
        VALUES (v_idorden, v_producto.idproducto, v_producto.cantidad, v_preciounitario);

        -- Actualizar el stock del producto
        UPDATE productos
        SET stock = stock - v_producto.cantidad
        WHERE idproducto = v_producto.idproducto;

        -- Verificar que el stock no sea negativo
        IF (SELECT stock FROM productos WHERE idproducto = v_producto.idproducto) < 0 THEN
            RAISE EXCEPTION 'Stock insuficiente para el producto con ID %', v_producto.idproducto;
        END IF;
    END LOOP;

    -- Calcular el total de la orden
    UPDATE ordenes
    SET total = (SELECT SUM(cantidad * preciounitario) FROM detalleordenes WHERE idorden = v_idorden)
    WHERE idorden = v_idorden;
END;
$$;


-- Procedimiento ID 19
CREATE OR REPLACE PROCEDURE apply_category_discount(
    p_category_id BIGINT,
    p_discount_percentage DECIMAL
)
    LANGUAGE plpgsql
AS $$
BEGIN
    -- Update productos table
    UPDATE productos p
    SET precio = precio * (1 - p_discount_percentage / 100)
    WHERE p.idcategoria = p_category_id
      AND p.idproducto NOT IN (
        -- Subquery to get products sold in the last 30 days
        SELECT DISTINCT do_.idproducto
        FROM detalleordenes do_
                 INNER JOIN ordenes o ON do_.idorden = o.idorden
        WHERE o.fechaorden >= CURRENT_TIMESTAMP - INTERVAL '30 days'
    );

    -- Commit the transaction
    COMMIT;

    -- Raise notice with number of affected products
    RAISE NOTICE 'Discount of % percent applied to inactive products in category %',
        p_discount_percentage,
        p_category_id;
END;
$$;
