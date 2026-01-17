drop database if exists crudUsuario;
create database if not exists crudUsuario;
use crudUsuario;
CREATE TABLE usuario(

id int primary key auto_increment not null ,

nombre varchar(100),

apellidos varchar(200),

email varchar(100) unique not null,

password varchar(200) not null,

activo TINYINT (1) DEFAULT 1


);

insert into usuario ( nombre, apellidos, email, password) values (
                                                                  'Rafa', 'Medina Ayuso', 'rafa@gmail.com', '$2a$12$CzdU7n41SWsBnlPVerrdu.rAHZGeeYxXqkZYyyraM0YxrT6ry4b.2'
                                                                 )

SELECT * FROM usuario;

