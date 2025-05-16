
-- ============================================================
-- MotoShop – PostgreSQL schema (versión inicial)
-- Generado: 2025-05-12 04:01:41 UTC
-- ============================================================

-- ==================
--   Tabla usuario
-- ==================
CREATE TABLE usuario (
    cedula          VARCHAR(10) PRIMARY KEY,
    email           VARCHAR(100) UNIQUE NOT NULL,
    password_hash   VARCHAR(255) NOT NULL,
    rol             VARCHAR(20) NOT NULL,
    fecha_creacion  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ==================
--   Tabla administrador
-- ==================
CREATE TABLE administrador (
    id_administrador SERIAL PRIMARY KEY,
    cedula           VARCHAR(10) NOT NULL,
    nombre_completo  VARCHAR(100) NOT NULL,
    CONSTRAINT fk_admin_usuario FOREIGN KEY (cedula)
        REFERENCES usuario (cedula)
        ON DELETE RESTRICT
);

-- ==================
--   Tabla cliente
-- ==================
CREATE TABLE cliente (
    id_cliente SERIAL PRIMARY KEY,
    cedula     VARCHAR(10) UNIQUE,
    nombre     VARCHAR(100),
    telefono   VARCHAR(20),
    CONSTRAINT fk_cliente_usuario FOREIGN KEY (cedula)
        REFERENCES usuario (cedula)
        ON DELETE RESTRICT
);

-- ==================
--   Tabla direccion
-- ==================
CREATE TABLE direccion (
    id_direccion SERIAL PRIMARY KEY,
    id_cliente   INT,
    calle        VARCHAR(200),
    ciudad       VARCHAR(100),
    provincia    VARCHAR(100),
    codigo_postal VARCHAR(20),
    CONSTRAINT fk_direccion_cliente FOREIGN KEY (id_cliente)
        REFERENCES cliente (id_cliente)
        ON DELETE CASCADE
);

-- ==================
--   Tabla categoria
-- ==================
CREATE TABLE categoria (
    id_categoria SERIAL PRIMARY KEY,
    nombre_categoria VARCHAR(50) UNIQUE NOT NULL
);

-- ==================
--   Tabla producto
-- ==================
CREATE TABLE producto (
    id_producto  SERIAL PRIMARY KEY,
    id_categoria INT NOT NULL,
    nombre       VARCHAR(100) NOT NULL,
    descripcion  VARCHAR(100),
    precio       NUMERIC(10,2) NOT NULL,
    stock        INT NOT NULL,
    imagen_principal VARCHAR(500),
    CONSTRAINT fk_producto_categoria FOREIGN KEY (id_categoria)
        REFERENCES categoria (id_categoria)
        ON DELETE RESTRICT
);

-- ==================
--   Tabla imagen_producto
-- ==================
CREATE TABLE imagen_producto (
    id_imagen   SERIAL PRIMARY KEY,
    id_producto INT NOT NULL,
    url_imagen  VARCHAR(500) NOT NULL,
    CONSTRAINT fk_imagen_producto FOREIGN KEY (id_producto)
        REFERENCES producto (id_producto)
        ON DELETE CASCADE
);

-- ==================
--   Tabla pedido
-- ==================
CREATE TABLE pedido (
    id_pedido    SERIAL PRIMARY KEY,
    id_cliente   INT NOT NULL,
    fecha_pedido TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total        NUMERIC(10,2),
    estado       VARCHAR(50),
    id_direccion INT,
    id_transaccion INT,
    CONSTRAINT fk_pedido_cliente FOREIGN KEY (id_cliente)
        REFERENCES cliente (id_cliente)
        ON DELETE CASCADE,
    CONSTRAINT fk_pedido_direccion FOREIGN KEY (id_direccion)
        REFERENCES direccion (id_direccion)
        ON DELETE SET NULL
);

-- ==================
--   Tabla detalle_pedido
-- ==================
CREATE TABLE detalle_pedido (
    id_detalle     SERIAL PRIMARY KEY,
    id_pedido      INT,
    id_producto    INT,
    cantidad       INT NOT NULL,
    precio_unitario NUMERIC(10,2) NOT NULL,
    CONSTRAINT fk_detalle_pedido FOREIGN KEY (id_pedido)
        REFERENCES pedido (id_pedido)
        ON DELETE CASCADE,
    CONSTRAINT fk_detalle_producto FOREIGN KEY (id_producto)
        REFERENCES producto (id_producto)
        ON DELETE RESTRICT
);

-- ==================
--   Tabla factura
-- ==================
CREATE TABLE factura (
    id_factura   SERIAL PRIMARY KEY,
    id_pedido    INT NOT NULL,
    fecha_factura TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total        NUMERIC(10,2),
    CONSTRAINT fk_factura_pedido FOREIGN KEY (id_pedido)
        REFERENCES pedido (id_pedido)
        ON DELETE CASCADE
);

-- ==================
--   Tabla cuenta_bancaria
-- ==================
CREATE TABLE cuenta_bancaria (
    id_cuenta     SERIAL PRIMARY KEY,
    cliente_id    INT,
    tipo_cuenta   VARCHAR(20),
    saldo         NUMERIC(18,2),
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_cuenta_cliente FOREIGN KEY (cliente_id)
        REFERENCES cliente (id_cliente)
        ON DELETE SET NULL
);

-- ==================
--   Tabla transaccion
-- ==================
CREATE TABLE transaccion (
    id_transaccion SERIAL PRIMARY KEY,
    cuenta_origen  INT,
    cuenta_destino INT,
    monto          NUMERIC(18,2),
    tipo_transaccion VARCHAR(30),
    fecha_transaccion TIMESTAMP,
    estado         VARCHAR(20),
    saldo_restante NUMERIC(18,2),
    CONSTRAINT fk_trans_origen FOREIGN KEY (cuenta_origen)
        REFERENCES cuenta_bancaria (id_cuenta)
        ON DELETE SET NULL,
    CONSTRAINT fk_trans_destino FOREIGN KEY (cuenta_destino)
        REFERENCES cuenta_bancaria (id_cuenta)
        ON DELETE SET NULL
);
