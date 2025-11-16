
CREATE DATABASE techdam;
USE techdam;

-- TABLA EMPLEADOS
CREATE TABLE empleados (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    departamento VARCHAR(100) NOT NULL,
    salario DECIMAL(10,2) NOT NULL,
    activo BOOLEAN DEFAULT TRUE
);

-- TABLA PROYECTOS
CREATE TABLE proyectos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    presupuesto DECIMAL(12,2) NOT NULL
);

-- TABLA ASIGNACIONES
CREATE TABLE asignaciones (
    id INT AUTO_INCREMENT PRIMARY KEY,
    empleado_id INT NOT NULL,
    proyecto_id INT NOT NULL,
    fecha DATE NOT NULL DEFAULT (CURDATE()),
    FOREIGN KEY (empleado_id) REFERENCES empleados(id),
    FOREIGN KEY (proyecto_id) REFERENCES proyectos(id),
    INDEX idx_emp (empleado_id),
    INDEX idx_proy (proyecto_id)
);
-- INSERTS EMPLEADOS (5)
INSERT INTO empleados(nombre,departamento,salario,activo) VALUES
('Ana','IT',1800,TRUE),
('Luis','IT',1900,TRUE),
('Marta','HR',1600,TRUE),
('Jorge','Ventas',1500,TRUE),
('Elena','IT',2100,TRUE);

-- INSERTS PROYECTOS (5)
INSERT INTO proyectos(nombre,presupuesto) VALUES
('Alpha',50000),
('Beta',30000),
('Gamma',20000),
('Delta',45000),
('Omega',15000);

-- INSERTS ASIGNACIONES (5)
INSERT INTO asignaciones(empleado_id,proyecto_id) VALUES
(1,1),(2,1),(3,2),(4,3),(5,4);

-- PROCEDIMIENTO 1: actualizar salario por departamento
DELIMITER $$
CREATE PROCEDURE actualizar_salario_departamento(
    IN p_departamento VARCHAR(100),   -- valor recibido: departamento a actualizar
    IN p_porcentaje DECIMAL(5,2),     -- valor recibido: % de incremento
    OUT p_actualizados INT            -- valor devuelto: nº de filas afectadas
)
BEGIN
    -- Actualiza salario solo de empleados activos del departamento indicado
    UPDATE empleados
    SET salario = salario * (1 + p_porcentaje/100)
    WHERE departamento = p_departamento
      AND activo = TRUE;

    -- Devuelve cuántos empleados fueron actualizados
    SET p_actualizados = ROW_COUNT();
END$$
DELIMITER ;

DELIMITER $$

CREATE PROCEDURE actualizar_presupuesto(
    IN p_proyecto_id INT,
    IN p_nuevo_presupuesto DECIMAL(12,2)
)
BEGIN
    UPDATE proyectos
    SET presupuesto = p_nuevo_presupuesto
    WHERE id = p_proyecto_id;
END$$

DELIMITER ;

-- FUNCIÓN: contar empleados activos
DELIMITER $$
CREATE FUNCTION contar_empleados_activos()
RETURNS INT
DETERMINISTIC                       -- garantiza mismo resultado con misma BD
BEGIN
    -- Devuelve el número total de empleados activos
    RETURN (SELECT COUNT(*) 
            FROM empleados 
            WHERE activo = TRUE);
END$$
DELIMITER ;
