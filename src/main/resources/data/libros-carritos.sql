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
-- LIBROS ADICIONALES (13 DISPONIBLE) para la paginacion del catalogo
-- Con los 5 anteriores quedan 18 libros disponibles: 2 paginas de 9
-- ---------------------------------------------------------------------
INSERT INTO libros
(id_libro, titulo, id_autor, isbn, descripcion, numero_paginas,
 precio_monto, precio_moneda, stock, edicion, id_editorial,
 fecha_publicacion, categoria, imagen, estado, eliminado, version)
VALUES
    ('b0000000-0000-0000-0000-000000000010',
     'Patrones de Diseno con Java',
     'a0000000-0000-0000-0000-000000000001',
     '9789580010110',
     'Catalogo de patrones explicados con ejemplos en Java.',
     350, 39.90, 'USD', 18, 'Primera edicion',
     'e0000000-0000-0000-0000-000000000001',
     '2020-05-14', 'PROGRAMACION', 'https://imagenes.test/libro-10.png',
     'DISPONIBLE', FALSE, 0),

    ('b0000000-0000-0000-0000-000000000011',
     'Bases de Datos Relacionales',
     'a0000000-0000-0000-0000-000000000001',
     '9789580010111',
     'Modelado, normalizacion y consultas SQL paso a paso.',
     410, 72000.00, 'COP', 10, 'Segunda edicion',
     'e0000000-0000-0000-0000-000000000001',
     '2018-09-30', 'TECNOLOGIA', 'https://imagenes.test/libro-11.png',
     'DISPONIBLE', FALSE, 0),

    ('b0000000-0000-0000-0000-000000000012',
     'Angular desde Cero',
     'a0000000-0000-0000-0000-000000000001',
     '9789580010112',
     'Aplicaciones web con componentes, servicios y rutas.',
     300, 29.50, 'USD', 25, 'Primera edicion',
     'e0000000-0000-0000-0000-000000000001',
     '2023-02-10', 'PROGRAMACION', 'https://imagenes.test/libro-12.png',
     'DISPONIBLE', FALSE, 0),

    ('b0000000-0000-0000-0000-000000000013',
     'Seguridad en Aplicaciones Web',
     'a0000000-0000-0000-0000-000000000001',
     '9789580010113',
     'Autenticacion, tokens y buenas practicas de seguridad.',
     280, 34.00, 'USD', 9, 'Primera edicion',
     'e0000000-0000-0000-0000-000000000001',
     '2022-07-19', 'TECNOLOGIA', 'https://imagenes.test/libro-13.png',
     'DISPONIBLE', FALSE, 0),

    ('b0000000-0000-0000-0000-000000000014',
     'Cafe al Amanecer',
     'a0000000-0000-0000-0000-000000000002',
     '9789580010114',
     'Historia de una familia cafetera durante una cosecha dificil.',
     240, 45000.00, 'COP', 14, 'Primera edicion',
     'e0000000-0000-0000-0000-000000000002',
     '2019-10-03', 'NOVELA', 'https://imagenes.test/libro-14.png',
     'DISPONIBLE', FALSE, 0),

    ('b0000000-0000-0000-0000-000000000015',
     'Cartas desde la Montana',
     'a0000000-0000-0000-0000-000000000002',
     '9789580010115',
     'Dos jovenes se escriben durante un largo invierno.',
     220, 16.90, 'USD', 11, 'Primera edicion',
     'e0000000-0000-0000-0000-000000000002',
     '2021-01-25', 'ROMANCE', 'https://imagenes.test/libro-15.png',
     'DISPONIBLE', FALSE, 0),

    ('b0000000-0000-0000-0000-000000000016',
     'Sombras en el Muelle',
     'a0000000-0000-0000-0000-000000000002',
     '9789580010116',
     'Un detective investiga desapariciones en un puerto.',
     310, 21.00, 'USD', 7, 'Primera edicion',
     'e0000000-0000-0000-0000-000000000002',
     '2020-11-08', 'MISTERIO', 'https://imagenes.test/libro-16.png',
     'DISPONIBLE', FALSE, 0),

    ('b0000000-0000-0000-0000-000000000017',
     'El Ultimo Guardian',
     'a0000000-0000-0000-0000-000000000002',
     '9789580010117',
     'Un joven hereda la tarea de proteger un bosque magico.',
     450, 27.75, 'USD', 16, 'Primera edicion',
     'e0000000-0000-0000-0000-000000000002',
     '2022-03-30', 'FANTASIA', 'https://imagenes.test/libro-17.png',
     'DISPONIBLE', FALSE, 0),

    ('b0000000-0000-0000-0000-000000000018',
     'Cuentos para Dormir',
     'a0000000-0000-0000-0000-000000000002',
     '9789580010118',
     'Relatos cortos para leer antes de dormir.',
     96, 28000.00, 'COP', 30, 'Primera edicion',
     'e0000000-0000-0000-0000-000000000002',
     '2017-06-12', 'INFANTIL', 'https://imagenes.test/libro-18.png',
     'DISPONIBLE', FALSE, 0),

    ('b0000000-0000-0000-0000-000000000019',
     'Breve Historia de la Ciencia',
     'a0000000-0000-0000-0000-000000000003',
     '9789580010119',
     'Los grandes descubrimientos contados de forma sencilla.',
     360, 24.00, 'USD', 13, 'Primera edicion',
     'e0000000-0000-0000-0000-000000000001',
     '2016-08-21', 'CIENCIA', 'https://imagenes.test/libro-19.png',
     'DISPONIBLE', FALSE, 0),

    ('b0000000-0000-0000-0000-000000000020',
     'Filosofia para Principiantes',
     'a0000000-0000-0000-0000-000000000003',
     '9789580010120',
     'Ideas de grandes pensadores en lenguaje claro.',
     270, 19.90, 'USD', 12, 'Primera edicion',
     'e0000000-0000-0000-0000-000000000002',
     '2015-04-17', 'FILOSOFIA', 'https://imagenes.test/libro-20.png',
     'DISPONIBLE', FALSE, 0),

    ('b0000000-0000-0000-0000-000000000021',
     'Salud y Bienestar Diario',
     'a0000000-0000-0000-0000-000000000003',
     '9789580010121',
     'Habitos simples para cuidar el cuerpo y la mente.',
     200, 35000.00, 'COP', 20, 'Primera edicion',
     'e0000000-0000-0000-0000-000000000001',
     '2021-09-09', 'SALUD', 'https://imagenes.test/libro-21.png',
     'DISPONIBLE', FALSE, 0),

    ('b0000000-0000-0000-0000-000000000022',
     'Educar con Proposito',
     'a0000000-0000-0000-0000-000000000003',
     '9789580010122',
     'Reflexiones y practicas para docentes y familias.',
     230, 26.40, 'USD', 8, 'Primera edicion',
     'e0000000-0000-0000-0000-000000000002',
     '2024-01-15', 'EDUCACION', 'https://imagenes.test/libro-22.png',
     'DISPONIBLE', FALSE, 0);

-- ---------------------------------------------------------------------
-- CARRITO en estado CREADO del Cliente 1 (un solo carrito activo por usuario)
-- El total se calcula en el dominio a partir de los items (todo en USD):
--   2 x 45.00 = 90.00  +  1 x 18.50 = 18.50  =>  108.50
-- ---------------------------------------------------------------------
INSERT INTO carritos
(id_carrito, id_usuario, estado, eliminado)
VALUES
    ('ca000000-0000-0000-0000-000000000001',
     'c0000000-0000-0000-0000-000000000001',
     'CREADO', FALSE);

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

-- descuento_libro: se aplica el descuento modificando el precio final
UPDATE libros SET
                  descuento_porcentaje = 20,
                  descuento_motivo = 'Temporada navideña',
                  descuento_fecha_inicio = '2026-11-01',
                  descuento_fecha_fin = '2026-12-31'
WHERE id_libro = 'b0000000-0000-0000-0000-000000000002';

UPDATE libros SET
                  descuento_porcentaje = 15,
                  descuento_motivo = 'Mes del libro',
                  descuento_fecha_inicio = '2026-11-01',
                  descuento_fecha_fin = '2026-12-31'
WHERE id_libro = 'b0000000-0000-0000-0000-000000000005';