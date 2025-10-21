-- Versión 2: Semilla de Roles y Administrador Inicial

-- 1. Insertar los roles definidos por el sistema
INSERT INTO rol (nombre) VALUES ('ADMIN');
INSERT INTO rol (nombre) VALUES ('CLIENTE');
INSERT INTO rol (nombre) VALUES ('STAFF');

-- 2. Insertar el usuario administrador inicial
-- El password es 'admin', encriptado con BCrypt
-- (Hash: $2a$10$f/p0.pE43R/N6S.LR.jPHe4.wW/d1qgNw1d.fG/jPzE0eD.y.Cg/y)
INSERT INTO usuario (username, password_bcrypt, enabled)
VALUES ('admin', '$2a$10$f/p0.pE43R/N6S.LR.jPHe4.wW/d1qgNw1d.fG/jPzE0eD.y.Cg/y', true);

-- 3. Asignar el rol 'ADMIN' al usuario 'admin'
-- Aseguramos que los IDs sean correctos usando sub-consultas
INSERT INTO usuario_rol (usuario_id, rol_id)
VALUES (
           (SELECT id FROM usuario WHERE username = 'admin'),
           (SELECT id FROM rol WHERE nombre = 'ADMIN')
       );