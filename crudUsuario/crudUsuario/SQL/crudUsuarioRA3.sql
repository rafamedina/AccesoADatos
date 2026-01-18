drop database if exists crudUsuario3;
create database if not exists crudUsuario3;
use crudUsuario3;
CREATE TABLE usuario(

id int primary key auto_increment not null ,

nombre varchar(100),

apellidos varchar(200),

nombre_usuario varchar(100),

email varchar(100) unique not null,

password varchar(200) not null,

activo TINYINT(1) DEFAULT 1,

fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

ultimo_login  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

contador_intentos int default 0 not null ,

estado TINYINT(1) default 1 not null

);

insert into usuario ( nombre, apellidos, nombre_usuario, email, password) values (
                                                                  'Rafa', 'Medina Ayuso', 'ikran','rafa@gmail.com', '$2a$12$CzdU7n41SWsBnlPVerrdu.rAHZGeeYxXqkZYyyraM0YxrT6ry4b.2'
                                                                 );

SELECT * FROM usuario;

UPDATE usuario
SET contador_intentos = 0,
    estado = 1
WHERE email = 'rafa@gmail.com';