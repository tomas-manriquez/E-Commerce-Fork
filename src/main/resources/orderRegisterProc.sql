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
