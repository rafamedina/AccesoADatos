/* ===============================
   BD: empresa_horas
   =============================== */

DROP DATABASE IF EXISTS empresa_horas;
CREATE DATABASE empresa_horas;
USE empresa_horas;

/* ===============================
   TABLA EMPLEADO
   =============================== */
CREATE TABLE empleado (
                          id_empleado BIGINT PRIMARY KEY,
                          nombre VARCHAR(100) NOT NULL,
                          departamento VARCHAR(100) NOT NULL
);

/* ===============================
   TABLA PROYECTO
   =============================== */
CREATE TABLE proyecto (
                          id_proyecto BIGINT PRIMARY KEY,
                          nombre VARCHAR(150) NOT NULL,
                          cliente VARCHAR(100) NOT NULL,
                          fecha_inicio DATE NOT NULL
);

/* ===============================
   TABLA TAREA
   =============================== */
CREATE TABLE tarea (
                       id_tarea BIGINT AUTO_INCREMENT PRIMARY KEY,
                       id_empleado BIGINT NOT NULL,
                       id_proyecto BIGINT NOT NULL,
                       horas INT NOT NULL CHECK (horas > 0),

                       CONSTRAINT fk_tarea_empleado
                           FOREIGN KEY (id_empleado)
                               REFERENCES empleado(id_empleado)
                               ON DELETE CASCADE
                               ON UPDATE CASCADE,

                       CONSTRAINT fk_tarea_proyecto
                           FOREIGN KEY (id_proyecto)
                               REFERENCES proyecto(id_proyecto)
                               ON DELETE CASCADE
                               ON UPDATE CASCADE
);

/* ===============================
   DATOS INICIALES
   =============================== */

/* EMPLEADOS (5) */
INSERT INTO empleado VALUES
                         (1, 'Ana López', 'IT'),
                         (2, 'Bruno García', 'Operaciones'),
                         (3, 'Carla Ruiz', 'Finanzas'),
                         (4, 'Diego Martín', 'Marketing'),
                         (5, 'Elena Pérez', 'RRHH');

/* PROYECTOS (10) */
INSERT INTO proyecto VALUES
                         (101, 'ERP Corporativo', 'ACME', '2025-01-10'),
                         (102, 'App Clientes', 'Globex', '2025-02-01'),
                         (103, 'Migración Cloud', 'Initech', '2025-03-05'),
                         (104, 'Data Warehouse', 'Umbrella', '2025-04-12'),
                         (105, 'Web Corporativa', 'Stark', '2025-05-20'),
                         (106, 'Automatización QA', 'Wayne', '2025-06-03'),
                         (107, 'CRM Ventas', 'Wonka', '2025-07-15'),
                         (108, 'BI Dashboards', 'Cyberdyne', '2025-08-01'),
                         (109, 'Soporte Plataforma', 'Soylent', '2025-09-09'),
                         (110, 'Formación Interna', 'Interno', '2025-10-01');

/* TAREAS (ejemplo de horas trabajadas) */
INSERT INTO tarea (id_empleado, id_proyecto, horas) VALUES
                                                        (1, 101, 8),
                                                        (1, 102, 6),
                                                        (2, 103, 7),
                                                        (3, 104, 5),
                                                        (4, 105, 4),
                                                        (5, 110, 3),
                                                        (2, 106, 6),
                                                        (3, 108, 8);

/* ===============================
   CONSULTAS DE COMPROBACIÓN
   =============================== */

-- Total de horas por empleado
SELECT e.nombre, SUM(t.horas) AS total_horas
FROM empleado e
         JOIN tarea t ON e.id_empleado = t.id_empleado
GROUP BY e.id_empleado;

-- Total de horas por proyecto
SELECT p.nombre, SUM(t.horas) AS total_horas
FROM proyecto p
         JOIN tarea t ON p.id_proyecto = t.id_proyecto
GROUP BY p.id_proyecto;
