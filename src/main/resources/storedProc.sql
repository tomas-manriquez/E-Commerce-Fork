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