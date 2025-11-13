
CREATE DATABASE empresa2;
USE empresa2;

CREATE TABLE empleados (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    salario DECIMAL(10,2) NOT NULL
);

CREATE TABLE proyectos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    presupuesto DECIMAL(10,2) NOT NULL
);

CREATE TABLE asignaciones (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_empleado INT,
    id_proyecto INT,
    fecha_asignacion DATE,
    FOREIGN KEY (id_empleado) REFERENCES empleados(id),
    FOREIGN KEY (id_proyecto) REFERENCES proyectos(id)
);






DELIMITER //

CREATE PROCEDURE AsignarEmpleadoAProyecto (
    IN p_id_empleado INT,
    IN p_id_proyecto INT
)
BEGIN
    DECLARE v_empleado_existe INT;
    DECLARE v_proyecto_existe INT;
    DECLARE v_asignacion_existe INT;

    -- 1. Verificar si el empleado existe
    SELECT COUNT(*) INTO v_empleado_existe
    FROM empleados
    WHERE id = p_id_empleado;

    -- 2. Verificar si el proyecto existe
    SELECT COUNT(*) INTO v_proyecto_existe
    FROM proyectos
    WHERE id = p_id_proyecto;

    -- 3. Verificar si ya existe la asignación
    SELECT COUNT(*) INTO v_asignacion_existe
    FROM asignaciones
    WHERE id_empleado = p_id_empleado AND id_proyecto = p_id_proyecto;

    -- Lógica de inserción y manejo de errores
    IF v_empleado_existe = 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Error: El ID de empleado especificado no existe en la tabla empleados.';
    ELSEIF v_proyecto_existe = 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Error: El ID de proyecto especificado no existe en la tabla proyectos.';
    ELSEIF v_asignacion_existe > 0 THEN
        -- Si la asignación ya existe, no hacemos nada o mostramos una advertencia.
        SELECT CONCAT('Advertencia: El empleado ', p_id_empleado, ' ya está asignado al proyecto ', p_id_proyecto) AS Mensaje;
    ELSE
        -- Insertar la nueva asignación
        INSERT INTO asignaciones (id_empleado, id_proyecto, fecha_asignacion)
        VALUES (p_id_empleado, p_id_proyecto, CURDATE());
        
        SELECT CONCAT('Asignación exitosa: Empleado ', p_id_empleado, ' asignado a Proyecto ', p_id_proyecto) AS Mensaje;
    END IF;

END //

DELIMITER ;