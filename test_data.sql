-- Test data for MotoShop Amazon Core Integration Testing

-- Insert test users
INSERT INTO usuario (cedula, nombre_usuario, email, password_hash, rol, fecha_creacion)
VALUES 
('1234567890', 'test.user1', 'test1@example.com', '$2a$10$xn3LI/AjqicFYZFruSwve.681477XaVNaUQbr1gioaWPn4t1KsnmG', 'ROLE_CLIENTE', CURRENT_TIMESTAMP),
('0987654321', 'test.user2', 'test2@example.com', '$2a$10$xn3LI/AjqicFYZFruSwve.681477XaVNaUQbr1gioaWPn4t1KsnmG', 'ROLE_CLIENTE', CURRENT_TIMESTAMP);

-- Insert test clients
INSERT INTO cliente (nombre, telefono, id_usuario)
VALUES 
('Test Cliente 1', '0991234567', (SELECT cedula FROM usuario WHERE cedula = '1234567890')),
('Test Cliente 2', '0997654321', (SELECT cedula FROM usuario WHERE cedula = '0987654321'));

-- Insert test addresses
INSERT INTO direccion (direccion, ciudad, codigo_postal, principal, id_cliente)
VALUES 
('Av. Principal 123', 'Quito', '170150', TRUE, (SELECT id FROM cliente WHERE nombre = 'Test Cliente 1')),
('Calle Secundaria 456', 'Guayaquil', '090150', TRUE, (SELECT id FROM cliente WHERE nombre = 'Test Cliente 2'));

-- Insert test categories
INSERT INTO categoria (nombre, descripcion)
VALUES 
('Cascos', 'Protección para la cabeza'),
('Accesorios', 'Accesorios para motos'),
('Repuestos', 'Repuestos para motos');

-- Insert test products with varying stock levels
INSERT INTO producto (nombre, descripcion, precio, stock, imagen_principal, id_categoria)
VALUES 
('Casco Integral Negro', 'Casco integral de alta resistencia', 120.00, 50, 'https://example.com/casco1.jpg', (SELECT id FROM categoria WHERE nombre = 'Cascos')),
('Casco Abierto Rojo', 'Casco abierto con visor', 80.00, 5, 'https://example.com/casco2.jpg', (SELECT id FROM categoria WHERE nombre = 'Cascos')),
('Guantes Protectores', 'Guantes de cuero con protección', 35.00, 0, 'https://example.com/guantes.jpg', (SELECT id FROM categoria WHERE nombre = 'Accesorios')),
('Aceite Motor', 'Aceite sintético para motor', 25.50, 100, 'https://example.com/aceite.jpg', (SELECT id FROM categoria WHERE nombre = 'Repuestos')),
('Candado Seguridad', 'Candado de disco con alarma', 45.00, 15, 'https://example.com/candado.jpg', (SELECT id FROM categoria WHERE nombre = 'Accesorios'));

-- Insert test orders
INSERT INTO pedido (id_cliente, id_direccion, fecha_pedido, total, estado)
VALUES 
((SELECT id FROM cliente WHERE nombre = 'Test Cliente 1'), 
 (SELECT id FROM direccion WHERE direccion = 'Av. Principal 123'), 
 CURRENT_TIMESTAMP, 120.00, 'COMPLETADO');

-- Insert test order details
INSERT INTO detalle_pedido (id_pedido, id_producto, cantidad, precio_unitario)
VALUES 
((SELECT id FROM pedido WHERE id_cliente = (SELECT id FROM cliente WHERE nombre = 'Test Cliente 1')), 
 (SELECT id FROM producto WHERE nombre = 'Casco Integral Negro'), 
 1, 120.00);
