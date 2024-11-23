-- storedProcedures.sql

-- Procedimiento ID 17 Reporte de usuarios con más queries
/*
CREATE OR REPLACE PROCEDURE reporte_auditoria_productos()
LANGUAGE plpgsql
AS $$
BEGIN
    SELECT idusuario, operacion, COUNT(*) AS cantidad
    FROM auditoria_productos
    GROUP BY idusuario, operacion
    ORDER BY cantidad DESC;
END;
$$;
*/

-- Procedimiento ID 18
CREATE OR REPLACE PROCEDURE registrar_orden(
    p_idcliente BIGINT,
    p_estado VARCHAR(50),
    p_total DECIMAL(10, 2),
    p_productos JSON
)
LANGUAGE plpgsql
AS $$
DECLARE
v_idorden BIGINT; -- Variable para almacenar el ID de la nueva orden
    v_producto RECORD; -- Variable para iterar sobre los productos
BEGIN
    INSERT INTO ordenes (fechaorden, estado, idcliente, total) -- Inserta la nueva orden
    VALUES (CURRENT_TIMESTAMP, p_estado, p_idcliente, p_total)
        RETURNING idorden INTO v_idorden;

    FOR v_producto IN SELECT * FROM json_to_recordset(p_productos) AS ( -- Recorre el JSON de productos seleccionados y registrar el detalle en detalleordenes
            idproducto BIGINT,
            cantidad INT,
            preciounitario DECIMAL(10,2)
        ) LOOP
                  INSERT INTO detalleordenes (idorden, idproducto, cantidad, preciounitario) -- Insertar cada detalle de la orden
                  VALUES (v_idorden, v_producto.idproducto, v_producto.cantidad, v_producto.preciounitario);

        UPDATE productos -- Actualiza el stock del producto
        SET stock = stock - v_producto.cantidad
        WHERE idproducto = v_producto.idproducto;

        IF (SELECT stock FROM productos WHERE idproducto = v_producto.idproducto) < 0 THEN -- Verifica que el stock no sea negativo
                RAISE EXCEPTION 'Stock insuficiente para el producto con ID %', v_producto.idproducto;
        END IF;
    END LOOP;
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