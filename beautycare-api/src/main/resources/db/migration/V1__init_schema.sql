
-- Tabla: Rol
-- Almacena los roles del sistema (ADMIN, CLIENTE, STAFF)
CREATE TABLE rol (
                     id BIGSERIAL PRIMARY KEY,
                     nombre VARCHAR(50) NOT NULL UNIQUE
);

-- Tabla: Usuario
-- Almacena los usuarios para autenticación
CREATE TABLE usuario (
                         id BIGSERIAL PRIMARY KEY,
                         username VARCHAR(100) NOT NULL UNIQUE,
                         password_bcrypt VARCHAR(255) NOT NULL,
                         enabled BOOLEAN NOT NULL DEFAULT true
);

-- Tabla: UsuarioRol
-- Tabla pivote para la relación Muchos-a-Muchos entre Usuario y Rol
CREATE TABLE usuario_rol (
                             usuario_id BIGINT NOT NULL,
                             rol_id BIGINT NOT NULL,
                             PRIMARY KEY (usuario_id, rol_id),
                             FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE CASCADE,
                             FOREIGN KEY (rol_id) REFERENCES rol(id) ON DELETE CASCADE
);

-- Tabla: Cliente
-- Almacena la información de los clientes del centro
CREATE TABLE cliente (
                         id BIGSERIAL PRIMARY KEY,
                         nombre VARCHAR(150) NOT NULL,
                         celular VARCHAR(20),
                         email VARCHAR(100) UNIQUE
);

-- Tabla: Profesional
-- Almacena los profesionales que prestan servicios
CREATE TABLE profesional (
                             id BIGSERIAL PRIMARY KEY,
                             nombre VARCHAR(150) NOT NULL,
                             especialidad VARCHAR(100)
);

-- Tabla: Servicio
-- Catálogo de servicios ofrecidos
CREATE TABLE servicio (
                          id BIGSERIAL PRIMARY KEY,
                          nombre VARCHAR(150) NOT NULL,
                          precio NUMERIC(10, 2) NOT NULL,
                          duracion_min INT NOT NULL
);

-- Tabla: Cita
-- Registra las citas agendadas
CREATE TABLE cita (
                      id BIGSERIAL PRIMARY KEY,
                      cliente_id BIGINT NOT NULL,
                      profesional_id BIGINT NOT NULL,
                      servicio_id BIGINT NOT NULL,
                      fecha_hora_inicio TIMESTAMP NOT NULL,
                      fecha_hora_fin TIMESTAMP NOT NULL,
                      estado VARCHAR(50) NOT NULL, -- (Ej. PENDIENTE, REALIZADA, CANCELADA)
                      FOREIGN KEY (cliente_id) REFERENCES cliente(id),
                      FOREIGN KEY (profesional_id) REFERENCES profesional(id),
                      FOREIGN KEY (servicio_id) REFERENCES servicio(id)
);

-- Tabla: Pago
-- Registra los pagos asociados a las citas
CREATE TABLE pago (
                      id BIGSERIAL PRIMARY KEY,
                      cita_id BIGINT NOT NULL,
                      monto NUMERIC(10, 2) NOT NULL,
                      metodo VARCHAR(50), -- (Ej. EFECTIVO, TARJETA, YAPE)
                      fecha_hora TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                      FOREIGN KEY (cita_id) REFERENCES cita(id)
);