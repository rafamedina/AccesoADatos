drop database if exists crudUsuario;
create database if not exists crudUsuario;
use crudUsuario;
CREATE TABLE usuario(

id int primary key auto_increment not null ,

nombre varchar(100),

apellidos varchar(200),

nombre_usuario varchar(100),

email varchar(100) unique not null,

password varchar(200) not null,

activo TINYINT(1) DEFAULT 1,

fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);


CREATE TABLE roles(
                      id int primary key auto_increment not null ,
                      nombre_rol varchar(100)unique not null
);

CREATE TABLE roles_usuarios(
                              id int primary key auto_increment not null ,
                                id_usuario int,
                                id_rol int,
                                FOREIGN KEY (id_usuario) references usuario(id),
                                FOREIGN KEY (id_rol) references roles(id)
);

insert into usuario ( nombre, apellidos, nombre_usuario, email, password) values (
                                                                  'Rafa', 'Medina Ayuso', 'ikran','rafa@gmail.com', '$2a$12$CzdU7n41SWsBnlPVerrdu.rAHZGeeYxXqkZYyyraM0YxrT6ry4b.2'
                                                                 );

insert into usuario ( nombre, apellidos, nombre_usuario, email, password) values (
                                                                                     'Iker', 'Acevedo Donate', 'klan','iker@gmail.com', '$2a$12$CzdU7n41SWsBnlPVerrdu.rAHZGeeYxXqkZYyyraM0YxrT6ry4b.2'
                                                                                 );

insert into roles( nombre_rol) values ('admin');
insert into roles( nombre_rol) values ('user');
insert into roles_usuarios(id_usuario, id_rol) VALUES (1,1);
insert into roles_usuarios(id_usuario, id_rol) VALUES (2,2);


DROP TABLE IF EXISTS SPRING_SESSION_ATTRIBUTES;
DROP TABLE IF EXISTS SPRING_SESSION;

-- 2. Crear tabla principal de sesiones
CREATE TABLE SPRING_SESSION (
                                PRIMARY_ID CHAR(36) NOT NULL,
                                SESSION_ID CHAR(36) NOT NULL,
                                CREATION_TIME BIGINT NOT NULL,
                                LAST_ACCESS_TIME BIGINT NOT NULL,
                                MAX_INACTIVE_INTERVAL INT NOT NULL,
                                EXPIRY_TIME BIGINT NOT NULL,
                                PRINCIPAL_NAME VARCHAR(100),
                                CONSTRAINT PK_SPRING_SESSION PRIMARY KEY (PRIMARY_ID)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 3. Crear índices para rendimiento
CREATE UNIQUE INDEX SPRING_SESSION_IX1 ON SPRING_SESSION (SESSION_ID);
CREATE INDEX SPRING_SESSION_IX2 ON SPRING_SESSION (EXPIRY_TIME);
CREATE INDEX SPRING_SESSION_IX3 ON SPRING_SESSION (PRINCIPAL_NAME);

-- 4. Crear tabla de atributos (donde se guardan los datos de sesión)
CREATE TABLE SPRING_SESSION_ATTRIBUTES (
                                           SESSION_PRIMARY_ID CHAR(36) NOT NULL,
                                           ATTRIBUTE_NAME VARCHAR(200) NOT NULL,
                                           ATTRIBUTE_BYTES BLOB NOT NULL,
                                           CONSTRAINT PK_SPRING_SESSION_ATTRIBUTES PRIMARY KEY (SESSION_PRIMARY_ID, ATTRIBUTE_NAME),
                                           CONSTRAINT FK_SPRING_SESSION_ATTRIBUTES_SPRING_SESSION FOREIGN KEY (SESSION_PRIMARY_ID) REFERENCES SPRING_SESSION (PRIMARY_ID) ON DELETE CASCADE
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;
SELECT * FROM usuario;

