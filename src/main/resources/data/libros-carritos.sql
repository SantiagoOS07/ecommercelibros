-- =====================================================================
-- M-12 - Datos de prueba de Libro y Carrito
-- Concide con usuarios-autores.sql:
--   Cliente 1  : c0000000-0000-0000-0000-000000000001
--   Autor 1    : a0000000-0000-0000-0000-000000000001
--   Autor 2    : a0000000-0000-0000-0000-000000000002
--   Autor 3    : a0000000-0000-0000-0000-000000000003
--   Editorial 1: e0000000-0000-0000-0000-000000000001
--   Editorial 2: e0000000-0000-0000-0000-000000000002
-- =====================================================================

-- ---------------------------------------------------------------------
-- LIBROS (9): 5 DISPONIBLE, 2 AGOTADO, 1 DESCONTINUADO, 1 eliminado
-- ---------------------------------------------------------------------
INSERT INTO libros
(id_libro, titulo, id_autor, isbn, descripcion, numero_paginas,
 precio_monto, precio_moneda, stock, edicion, id_editorial,
 fecha_publicacion, categoria, imagen, estado, eliminado, version)
VALUES
    -- 1 DISPONIBLE (el del carrito de prueba)
    ('b0000000-0000-0000-0000-000000000001',
     'Arquitectura Limpia en la Practica',
     'a0000000-0000-0000-0000-000000000001',
     '9789580010011',
     'Guia practica para disenar sistemas mantenibles con capas bien separadas.',
     420, 45.00, 'USD', 15, 'Segunda edicion',
     'e0000000-0000-0000-0000-000000000001',
     '2019-03-15', 'PROGRAMACION', 'https://imagenes.test/arquitectura-limpia.png',
     'DISPONIBLE', FALSE, 0),

    -- 2 DISPONIBLE (precio en COP)
    ('b0000000-0000-0000-0000-000000000002',
     'Redes y Comunicaciones Modernas',
     'a0000000-0000-0000-0000-000000000001',
     '9789580010028',
     'Introduccion a protocolos, enrutamiento y seguridad en redes actuales.',
     380, 85000.00, 'COP', 8, 'Primera edicion',
     'e0000000-0000-0000-0000-000000000001',
     '2021-08-02', 'TECNOLOGIA', 'https://imagenes.test/redes-modernas.png',
     'DISPONIBLE', FALSE, 0),

    -- 3 DISPONIBLE (el segundo item del carrito de prueba)
    ('b0000000-0000-0000-0000-000000000003',
     'La Casa del Rio',
     'a0000000-0000-0000-0000-000000000002',
     '9789580010035',
     'Novela familiar ambientada en un pueblo a orillas de un rio.',
     290, 18.50, 'USD', 20, 'Primera edicion',
     'e0000000-0000-0000-0000-000000000002',
     '2015-11-20', 'NOVELA', 'https://imagenes.test/casa-del-rio.png',
     'DISPONIBLE', FALSE, 0),

    -- 4 DISPONIBLE con poco stock (sirve para probar stock insuficiente)
    ('b0000000-0000-0000-0000-000000000004',
     'Historia Economica de Colombia',
     'a0000000-0000-0000-0000-000000000003',
     '9789580010042',
     'Recorrido por los ciclos economicos del pais desde la colonia.',
     510, 62000.00, 'COP', 5, 'Tercera edicion',
     'e0000000-0000-0000-0000-000000000002',
     '2018-06-10', 'HISTORIA', 'https://imagenes.test/historia-economica.png',
     'DISPONIBLE', FALSE, 0),

    -- 5 DISPONIBLE
    ('b0000000-0000-0000-0000-000000000005',
     'Fundamentos de Fisica Cuantica',
     'a0000000-0000-0000-0000-000000000003',
     '9789580010059',
     'Conceptos basicos explicados con ejemplos cotidianos.',
     330, 32.00, 'USD', 12, 'Primera edicion',
     'e0000000-0000-0000-0000-000000000001',
     '2020-02-28', 'CIENCIA', 'https://imagenes.test/fisica-cuantica.png',
     'DISPONIBLE', FALSE, 0),

    -- 6 AGOTADO (stock 0)
    ('b0000000-0000-0000-0000-000000000006',
     'El Misterio de la Calle Ocho',
     'a0000000-0000-0000-0000-000000000002',
     '9789580010066',
     'Novela policiaca con un giro final inesperado.',
     260, 22.00, 'USD', 0, 'Primera edicion',
     'e0000000-0000-0000-0000-000000000002',
     '2017-09-05', 'MISTERIO', 'https://imagenes.test/calle-ocho.png',
     'AGOTADO', FALSE, 0),

    -- 7 AGOTADO (stock 0, precio en COP)
    ('b0000000-0000-0000-0000-000000000007',
     'Versos de Medianoche',
     'a0000000-0000-0000-0000-000000000002',
     '9789580010073',
     'Antologia de poemas breves escritos de noche.',
     120, 38000.00, 'COP', 0, 'Primera edicion',
     'e0000000-0000-0000-0000-000000000001',
     '2016-04-23', 'POESIA', 'https://imagenes.test/versos-medianoche.png',
     'AGOTADO', FALSE, 0),

    -- 8 DESCONTINUADO (todavia con stock, pero ya no se vende)
    ('b0000000-0000-0000-0000-000000000008',
     'Arte Colonial Andino',
     'a0000000-0000-0000-0000-000000000003',
     '9789580010080',
     'Catalogo ilustrado de arte religioso de la region andina.',
     240, 55.00, 'USD', 3, 'Edicion especial',
     'e0000000-0000-0000-0000-000000000002',
     '2012-12-01', 'ARTE', 'https://imagenes.test/arte-colonial.png',
     'DESCONTINUADO', FALSE, 0),

    -- 9 ELIMINADO LOGICAMENTE (no debe salir en listados ni busquedas)
    ('b0000000-0000-0000-0000-000000000009',
     'Habitos Pequenos, Cambios Grandes',
     'a0000000-0000-0000-0000-000000000001',
     '9789580010097',
     'Libro retirado del catalogo: sirve para probar el eliminado logico.',
     200, 15.00, 'USD', 7, 'Primera edicion',
     'e0000000-0000-0000-0000-000000000001',
     '2014-01-17', 'AUTOAYUDA', 'https://imagenes.test/habitos-pequenos.png',
     'DISPONIBLE', TRUE, 0);

-- ---------------------------------------------------------------------
-- CARRITO en estado CREADO del Cliente 1 (un solo carrito activo por usuario)
-- precio_total = suma de los totales de los items (todo en USD):
--   2 x 45.00 = 90.00  +  1 x 18.50 = 18.50  =>  108.50
-- ---------------------------------------------------------------------
INSERT INTO carritos
(id_carrito, id_usuario, estado, precio_total, eliminado)
VALUES
    ('ca000000-0000-0000-0000-000000000001',
     'c0000000-0000-0000-0000-000000000001',
     'CREADO', 108.50, FALSE);

-- stock_libro = stock actual del libro; cantidad <= stock_libro
INSERT INTO items_carrito
(id_item_carrito, id_carrito, isbn_libro,
 precio_unitario_monto, precio_unitario_moneda,
 precio_total_monto, precio_total_moneda,
 stock_libro, cantidad)
VALUES
    ('1c000000-0000-0000-0000-000000000001',
     'ca000000-0000-0000-0000-000000000001',
     '9789580010011',
     45.00, 'USD', 90.00, 'USD', 15, 2),

    ('1c000000-0000-0000-0000-000000000002',
     'ca000000-0000-0000-0000-000000000001',
     '9789580010035',
     18.50, 'USD', 18.50, 'USD', 20, 1);