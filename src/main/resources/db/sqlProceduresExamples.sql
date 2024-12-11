CALL reporte_auditoria();

CALL apply_category_discount(4,10);

CALL apply_category_discount(1,10);

CALL registrar_orden(
    1, -- ID del cliente
    'pendiente', -- Estado de la orden
    '[{"idproducto": 1, "cantidad": 2}, {"idproducto": 2, "cantidad": 1}]'::json -- JSON con los productos
);

CALL registrar_orden(
    2, -- ID del cliente
    'pendiente', -- Estado de la orden
    '[{"idproducto": 3, "cantidad": 5}, {"idproducto": 4, "cantidad": 4}]'::json -- JSON con los productos
);

CALL reporte_auditoria();