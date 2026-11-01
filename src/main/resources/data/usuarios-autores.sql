-- =====================================================================
-- M-11 - Datos de prueba de Usuario, Autor y Editorial
-- Los UUID coinciden con libros-carritos.sql (autores a..., editoriales e..., cliente c...01)
--
-- Usuarios de prueba (las contraseñas están guardadas con BCrypt):
--   ADMINISTRADOR : admin@libros.com       / Admin12345
--   PROVEEDOR     : proveedor@libros.com   / Proveedor123
--   CLIENTE       : cliente1@libros.com    / Cliente123   (dueño del carrito de prueba)
--   CLIENTE       : cliente2@libros.com    / Cliente123
--   INACTIVO      : inactivo@libros.com    / Inactivo123  (no puede iniciar sesión)
--
-- El admin tiene el mismo correo y contraseña que app.seed.admin.*; como ya existe,
-- UsuariosIniciales no vuelve a crearlo.
-- =====================================================================

-- ---------------------------------------------------------------------
-- USUARIOS (5)
-- ---------------------------------------------------------------------
INSERT INTO usuarios
(id_usuario, nombre, correo, contrasenia_hash, telefono, direccion, rol_usuario, estado_usuario)
VALUES
    ('0a000000-0000-0000-0000-000000000001',
     'Administrador General', 'admin@libros.com',
     '{bcrypt}$2a$10$A/81BY4bzWG8qB/cQVfLnuWv1BwBxMGU/JRX9n9uiDU9Cf15cZDvO',
     '3000000000', 'Oficina principal', 'ADMINISTRADOR', 'ACTIVO'),

    ('f0000000-0000-0000-0000-000000000001',
     'Distribuidora Andina', 'proveedor@libros.com',
     '{bcrypt}$2a$10$exX/b8Y28sCN4RQo4F5yTeQ/Gljpo.lYNyjEVMkItOX/OvCB4Qhge',
     '3101112233', 'Calle 21 # 14-30, Armenia', 'PROVEEDOR', 'ACTIVO'),

    ('c0000000-0000-0000-0000-000000000001',
     'Laura Gomez', 'cliente1@libros.com',
     '{bcrypt}$2a$10$/lwvu63EUwh2tvFIiilR7.kbY97HwLkx4DMNUSOzoX0679DW42m9O',
     '3154445566', 'Carrera 14 # 20-35, Armenia', 'CLIENTE', 'ACTIVO'),

    ('c0000000-0000-0000-0000-000000000002',
     'Andres Rojas', 'cliente2@libros.com',
     '{bcrypt}$2a$10$/lwvu63EUwh2tvFIiilR7.kbY97HwLkx4DMNUSOzoX0679DW42m9O',
     '3207778899', 'Avenida Bolivar # 3-12, Calarca', 'CLIENTE', 'ACTIVO'),

    ('c0000000-0000-0000-0000-000000000003',
     'Cuenta Inactiva', 'inactivo@libros.com',
     '{bcrypt}$2a$10$Q9g/6e9x8WbaZHFR8FV89edcjlng6.6HXP932VpCwYXe0dKpUjeYe',
     '3009990000', 'Calle 50 # 10-10, Pereira', 'CLIENTE', 'INACTIVO');

-- ---------------------------------------------------------------------
-- AUTORES (4): 3 usados por los libros de prueba + 1 eliminado logicamente
-- ---------------------------------------------------------------------
INSERT INTO autores
(id_autor, nombre_autor, biografia, eliminado)
VALUES
    ('a0000000-0000-0000-0000-000000000001',
     'Camila Restrepo',
     'Ingeniera de software y docente universitaria; escribe sobre arquitectura y redes.',
     FALSE),

    ('a0000000-0000-0000-0000-000000000002',
     'Mateo Salazar',
     'Novelista y poeta; sus historias transcurren en pueblos del Eje Cafetero.',
     FALSE),

    ('a0000000-0000-0000-0000-000000000003',
     'Isabel Duarte',
     'Historiadora y divulgadora de ciencia.',
     FALSE),

    ('a0000000-0000-0000-0000-000000000004',
     'Autor Retirado',
     'Autor eliminado logicamente: no debe salir en listados ni consultas.',
     TRUE);

INSERT INTO autor_nacionalidades
(id_autor, nacionalidad)
VALUES
    ('a0000000-0000-0000-0000-000000000001', 'COLOMBIANA'),
    ('a0000000-0000-0000-0000-000000000001', 'ESPANOLA'),
    ('a0000000-0000-0000-0000-000000000002', 'COLOMBIANA'),
    ('a0000000-0000-0000-0000-000000000003', 'MEXICANA'),
    ('a0000000-0000-0000-0000-000000000003', 'ARGENTINA'),
    ('a0000000-0000-0000-0000-000000000004', 'CHILENA');

-- ---------------------------------------------------------------------
-- EDITORIALES (2): las que usan los libros de prueba
-- ---------------------------------------------------------------------
INSERT INTO editoriales
(id_editorial, nombre_editorial, pais_origen, descripcion_editorial)
VALUES
    ('e0000000-0000-0000-0000-000000000001',
     'Editorial Quindio Tecnico', 'COLOMBIA',
     'Libros de ingenieria, tecnologia y ciencias.'),

    ('e0000000-0000-0000-0000-000000000002',
     'Ediciones Cafe y Letras', 'COLOMBIA',
     'Narrativa, poesia e historia regional.');