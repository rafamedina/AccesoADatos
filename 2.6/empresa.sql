CREATE DATABASE IF NOT EXISTS empresa;
USE empresa;

CREATE TABLE empleados (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50),
    salario DOUBLE
);

CREATE TABLE proyectos(
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100),
    presupuesto DECIMAL(10,2)
);



INSERT INTO empleados (nombre, salario)
VALUES ('Ana', 25000), ('Luis', 28000), ('Marta', 32000);

DELIMITER //
CREATE PROCEDURE obtener_empleado(IN empId INT)
BEGIN
    SELECT id, nombre FROM empleados WHERE id = empId;
END //
DELIMITER ;

call obtener_empleado(2);

SELECT * from proyectos;

DELIMITER $$

CREATE PROCEDURE incrementar_salario(
    IN p_id INT,
    IN p_incremento DOUBLE,
    OUT p_nuevo_salario DOUBLE
)
BEGIN
    -- Actualizar el salario del empleado sumando el incremento
    UPDATE empleados
    SET salario = salario + p_incremento
    WHERE id = p_id;

    -- Obtener el nuevo salario y devolverlo en el parámetro de salida
    SELECT salario INTO p_nuevo_salario
    FROM empleados
    WHERE id = p_id;
END$$

DELIMITER ;

CREATE TABLE cuentas (
id INT AUTO_INCREMENT PRIMARY KEY,
titular VARCHAR(100),
saldo DECIMAL(10,2)
);
INSERT INTO cuentas (titular, saldo) VALUES
('Ana', 2000.00),
('Luis', 1500.00);

CREATE TABLE logs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    mensaje VARCHAR(255) NOT NULL,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

select * from logs;
