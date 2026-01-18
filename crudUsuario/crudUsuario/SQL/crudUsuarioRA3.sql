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

ultimoLogin  TIMESTAMP DEFAULT CURRENT_TIMESTAMP



);

insert into usuario ( nombre, apellidos, nombre_usuario, email, password) values (
                                                                  'Rafa', 'Medina Ayuso', 'ikran','rafa@gmail.com', '$2a$12$CzdU7n41SWsBnlPVerrdu.rAHZGeeYxXqkZYyyraM0YxrT6ry4b.2'
                                                                 );

SELECT * FROM usuario;

