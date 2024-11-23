-- populateDB.sql

insert into categorias (nombre) values ('Tecnologia');
insert into categorias (nombre) values ('Cocina');
insert into categorias (nombre) values ('Limpieza');
insert into categorias (nombre) values ('Jardin');
SELECT setval('public.categorias_idcategoria_seq', (SELECT MAX(idcategoria) FROM public.categorias), true);


-- Datos iniciales para la tabla productos
INSERT INTO productos (nombre, descripcion, precio, stock, estado, idcategoria) VALUES
('Smartphone Samsung Galaxy S21', 'Celular Samsung Galaxy S21, pantalla AMOLED de 6.2 pulgadas', 450000.00, 30, 'disponible', 1),
('Smartphone iPhone 12', 'Celular Apple iPhone 12 con 128GB de almacenamiento', 499000.00, 25, 'disponible', 1),
('Smartphone Xiaomi Redmi Note 11', 'Celular Xiaomi Redmi Note 11, cámara de 50MP', 220000.00, 40, 'disponible', 1),
('Smartphone Motorola Moto G Power', 'Motorola Moto G Power, batería de 5000mAh', 180000.00, 35, 'disponible', 1),
('Smartphone Huawei P40 Lite', 'Huawei P40 Lite, procesador Kirin y 6GB de RAM', 310000.00, 28, 'disponible', 1),
('Notebook HP Pavilion 15', 'Notebook HP Pavilion 15, procesador Intel i5, 8GB RAM', 500000.00, 22, 'disponible', 1),
('Notebook Dell Inspiron 3501', 'Notebook Dell Inspiron 3501, pantalla Full HD, SSD 256GB', 620000.00, 20, 'disponible', 1),
('Notebook Acer Aspire 5', 'Notebook Acer Aspire 5, AMD Ryzen 5, 16GB RAM', 550000.00, 25, 'disponible', 1),
('Notebook Lenovo IdeaPad 3', 'Notebook Lenovo IdeaPad 3, procesador AMD Ryzen 7', 680000.00, 30, 'disponible', 1),
('Notebook Asus VivoBook 14', 'Notebook Asus VivoBook 14, pantalla NanoEdge, 512GB SSD', 800000.00, 23, 'disponible', 1);


-- Datos iniciales para la categoría de cocina (idcategoria = 2)
INSERT INTO productos (nombre, descripcion, precio, stock, estado, idcategoria) VALUES
-- Microondas
('Microondas Samsung MG23K3513CK', 'Microondas Samsung con grill y capacidad de 23L', 120000.00, 30, 'disponible', 2),
('Microondas LG MH6535GIB', 'Microondas LG con tecnología Smart Inverter y 25L', 95000.00, 25, 'disponible', 2),
('Microondas Daewoo KOR-660', 'Microondas Daewoo compacto de 20L', 50000.00, 40, 'disponible', 2),
-- Hornos eléctricos
('Horno eléctrico Oster TSSTTVDFL2', 'Horno eléctrico Oster con capacidad de 22L', 60000.00, 20, 'disponible', 2),
('Horno eléctrico Black+Decker TO1760S', 'Horno eléctrico compacto de 14L', 45000.00, 35, 'disponible', 2),
('Horno eléctrico Ursus Trotter UT-HS25', 'Horno eléctrico con 25L y temporizador', 20000.00, 28, 'disponible', 2),
-- Panificadoras
('Panificadora Imaco PB860', 'Panificadora Imaco con 12 programas y capacidad de 1kg', 130000.00, 15, 'disponible', 2),
('Panificadora Oster CKSTBRTW20', 'Panificadora Oster con control de tostado', 100000.00, 25, 'disponible', 2),
('Panificadora Moulinex OW210130', 'Panificadora Moulinex para pan y masas', 60000.00, 18, 'disponible', 2);


-- Datos iniciales para la categoría de limpieza (idcategoria = 3)
INSERT INTO productos (nombre, descripcion, precio, stock, estado, idcategoria) VALUES
-- Aspiradoras
('Aspiradora LG CordZero A9', 'Aspiradora LG inalámbrica con motor Smart Inverter y 200W', 120000.00, 20, 'disponible', 3),
('Aspiradora Philips FC9352/01', 'Aspiradora Philips compacta sin bolsa y filtro HEPA', 95000.00, 25, 'disponible', 3),
('Aspiradora Samsung Jet 60', 'Aspiradora Samsung portátil y liviana con batería de larga duración', 80000.00, 18, 'disponible', 3),
('Aspiradora Thomas Aqua+', 'Aspiradora Thomas con sistema de filtrado por agua', 100000.00, 30, 'disponible', 3),
('Aspiradora Electrolux Ease C9', 'Aspiradora Electrolux con boquilla turbo para pelos de mascota', 50000.00, 15, 'disponible', 3),
-- Fregadoras inalámbricas
('Fregadora Xiaomi Mi Electric Mop', 'Fregadora inalámbrica Xiaomi con doble rotación y tanque de agua', 110000.00, 12, 'disponible', 3),
('Fregadora Kärcher FC 7', 'Fregadora inalámbrica Kärcher para pisos duros con batería recargable', 150000.00, 10, 'disponible', 3),
-- Aspiradoras robot
('Robot Aspiradora iRobot Roomba 692', 'Robot aspiradora iRobot con sistema de navegación inteligente', 200000.00, 15, 'disponible', 3),
('Robot Aspiradora Xiaomi Mi Robot Vacuum', 'Robot aspiradora Xiaomi con sensor láser y control desde app', 120000.00, 18, 'disponible', 3),
('Robot Aspiradora Cecotec Conga 1790', 'Robot aspiradora Cecotec con mapeo y función fregado', 80000.00, 20, 'disponible', 3);


-- Datos iniciales para la categoría de jardín (idcategoria = 4)
INSERT INTO productos (nombre, descripcion, precio, stock, estado, idcategoria) VALUES
-- Cortadoras de pasto
('Cortadora de Pasto Black+Decker BEMW451BH', 'Cortadora de pasto eléctrica Black+Decker con motor de 1200W', 150000.00, 10, 'disponible', 4),
('Cortadora de Pasto Hyundai LM3301', 'Cortadora de pasto manual Hyundai con ancho de corte de 33 cm', 80000.00, 8, 'disponible', 4),
('Cortadora de Pasto Einhell GC-EM 1030', 'Cortadora de pasto Einhell con sistema de ajuste de altura', 90000.00, 6, 'disponible', 4),
('Cortadora de Pasto Makita DLM460PT2', 'Cortadora de pasto a batería Makita con doble cuchilla', 320000.00, 5, 'disponible', 4),
('Cortadora de Pasto Gamma G1838', 'Cortadora de pasto Gamma compacta con motor de 1000W', 60000.00, 7, 'disponible', 4),
-- Pulverizadores
('Pulverizador Matabi Super Agro 16L', 'Pulverizador manual Matabi con capacidad de 16 litros', 40000.00, 8, 'disponible', 4),
('Pulverizador Stihl SG 31', 'Pulverizador de presión previa Stihl con capacidad de 5 litros', 65000.00, 5, 'disponible', 4),
('Pulverizador a Motor Husqvarna 321S25', 'Pulverizador Husqvarna a motor con capacidad de 25 litros', 210000.00, 5, 'disponible', 4),
-- Orilladoras
('Orilladora Black+Decker GL260', 'Orilladora eléctrica Black+Decker con motor de 350W', 50000.00, 7, 'disponible', 4),
('Orilladora Stihl FS 55', 'Orilladora Stihl a gasolina para uso intensivo', 220000.00, 5, 'disponible', 4);
SELECT setval('public.productos_idproducto_seq', (SELECT MAX(idproducto) FROM public.productos), true);



-- contraseñas en formato pass123, pass234, pass345 etc segun numeros del nombre de usuario
INSERT INTO public.clientes (idcliente, nombre, direccion, email, telefono, password, username, rol) VALUES
(1, 'nombre', 'direccion', 'admin@ecommerce.cl', 'telefono', '$2a$10$dZVr6KP5UyftVBA48HHbve4NHsD3CPzkjcu3dJBuwJuykEC2zoEIS', 'admin', 'ADMIN'),
(2, 'Juan Perez', 'Av. Siempre Viva 123', 'juan.perez@example.com', '987654321', '$2a$10$VVf/yAaV7YWAqLnTn3f3weO7uB/g4i0TZKedKPJvtEr87VE6yhIZm', 'juan123', 'USER'),
(3, 'María Lopez', 'Calle Falsa 456', 'maria.lopez@example.com', '987654322', '$2a$10$lkU792clrkZm8crs6jpQbu9DXqyuG4caZ6zIip34T3Q.LJvrB9shS', 'maria234', 'USER'),
(4, 'Carlos González', 'Pje. Los Alamos 789', 'carlos.gonzalez@example.com', '987654323', '$2a$10$3/e6BmfT11nd.La4FGHrF.LfBA2z0ZQhEaPelbNqxG52VlEEnrkZO', 'carlos345', 'USER'),
(5, 'Ana Ramírez', 'Calle Primavera 101', 'ana.ramirez@example.com', '987654324', '$2a$10$M9szaYukFgTQPjIMCtfV6OHRY56gL61qWt0izMsOeng9S4TJSVC/S', 'ana456', 'USER'),
(6, 'Pedro Soto', 'Av. El Bosque 202', 'pedro.soto@example.com', '987654325', '$2a$10$IqEV840R.55a1/oFFZDMiOFRNDXj1szi5mH6veL1H7.H.7QUD42cq', 'pedro567', 'USER'),
(7, 'Lucia Herrera', 'Pje. Las Rosas 303', 'lucia.herrera@example.com', '987654326', '$2a$10$UnyuFqlCgPwnyTpgpwjfru4CprAopmgc7E.2BYzGahCp.HT2ushEq', 'lucia678', 'USER');
SELECT setval('public.clientes_idcliente_seq', (SELECT MAX(idcliente) FROM public.clientes), true);

--Datos iniciales para ordenes de compra
INSERT INTO public.ordenes (idorden, fechaorden, estado, idcliente, total) VALUES
(1, '2024-11-23 12:00:00', 'pendiente', 1, 1399000.00),
(2, '2024-11-23 12:10:00', 'pendiente', 1, 3297000.00),
(3, '2024-11-23 12:20:00', 'pendiente', 2, 500000.00),
(4, '2024-05-23 12:30:00', 'pendiente', 3, 1750000.00),
(5, '2024-08-23 12:40:00', 'pendiente', 4, 15095000.00),
(6, '2024-09-23 12:50:00', 'pendiente', 5, 10880000.00),
(7, '2024-10-23 13:00:00', 'pendiente', 6, 1765000.00),
(8, '2024-11-23 13:10:00', 'pendiente', 1, 1530000.00),
SELECT setval('public.ordenes_idorden_seq', (SELECT MAX(idorden) FROM public.ordenes), true);

--Detalles correspondientes con las ordenes anteriores
INSERT INTO public.detalleordenes (idorden, idproducto, cantidad, preciounitario) VALUES
INSERT INTO public.detalleordenes (iddetalle, idorden, idproducto, cantidad, preciounitario) VALUES
(1, 2, 1, 2, 450000.00),
(2, 2, 2, 1, 499000.00),
(3, 3, 1, 4, 450000.00),
(4, 3, 2, 3, 499000.00),
(5, 4, 11, 1, 120000.00),
(6, 4, 14, 2, 60000.00),
(7, 4, 17, 2, 130000.00),
(8, 5, 20, 10, 120000.00),
(9, 5, 29, 5, 80000.00),
(10, 5, 30, 1, 150000.00),
(11, 6, 2, 5, 499000.00),
(12, 6, 6, 8, 500000.00),
(13, 6, 7, 5, 620000.00),
(14, 6, 8, 10, 550000.00),
(15, 7, 3, 4, 220000.00),
(16, 7, 1, 10, 450000.00),
(17, 7, 8, 10, 550000.00),
(18, 8, 4, 4, 180000.00),
(19, 8, 12, 4, 95000.00),
(20, 8, 21, 7, 95000.00),
(21, 9, 4, 7, 180000.00),
(22, 9, 15, 6, 45000.00);
SELECT setval('public.detalleordenes_iddetalle_seq', (SELECT MAX(iddetalle) FROM public.detalleordenes), true);
