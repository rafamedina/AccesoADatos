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