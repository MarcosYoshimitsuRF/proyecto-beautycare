-- Tabla: Proveedor
-- Almacena los proveedores de insumos
CREATE TABLE proveedor (
                           id BIGSERIAL PRIMARY KEY,
                           nombre VARCHAR(150) NOT NULL,
                           ruc VARCHAR(20) UNIQUE,
                           telefono VARCHAR(20)
);

-- Tabla: Insumo
-- Almacena los insumos del inventario
CREATE TABLE insumo (
                        id BIGSERIAL PRIMARY KEY,
                        nombre VARCHAR(150) NOT NULL,
                        stock INT NOT NULL DEFAULT 0,
                        stock_minimo INT NOT NULL DEFAULT 0,
                        unidad VARCHAR(50) -- (Ej. 'ml', 'gr', 'unidad')
);

-- Tabla: Compra
-- Registra las compras de insumos a proveedores
CREATE TABLE compra (
                        id BIGSERIAL PRIMARY KEY,
                        proveedor_id BIGINT,
                        fecha DATE NOT NULL DEFAULT CURRENT_DATE,
                        total NUMERIC(10, 2) NOT NULL,
                        FOREIGN KEY (proveedor_id) REFERENCES proveedor(id)
);

-- Tabla: ConsumoInsumo
-- Define cuánto de un insumo se consume por cada servicio
-- Esta es la tabla clave para la comunicación entre microservicios
CREATE TABLE consumo_insumo (
                                id BIGSERIAL PRIMARY KEY,

    -- Este es el ID del servicio en la BD 'beautycare_db'.
    -- Es un campo numérico sin Foreign Key porque la BD está separada. [cite: 57, 58, 59]
                                servicio_id BIGINT NOT NULL,

    -- Este SÍ es un Foreign Key a la tabla 'insumo' de esta misma BD
                                insumo_id BIGINT NOT NULL,
                                cantidad_por_servicio NUMERIC(10, 2) NOT NULL, -- Cantidad a descontar

                                FOREIGN KEY (insumo_id) REFERENCES insumo(id)
);