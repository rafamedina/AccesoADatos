create database if not exists crudUsuario;
use crudUsuario;
CREATE TABLE usuario(

                        id int primary key auto_increment not null ,

                        nombre varchar(100),

                        apellidos varchar(200),

                        email varchar(100) unique not null,

                        password varchar(200) not null
);



