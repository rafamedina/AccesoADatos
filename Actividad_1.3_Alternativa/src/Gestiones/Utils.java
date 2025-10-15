package Gestiones;

import java.util.Scanner;

public class Utils {
    Scanner sc = new Scanner(System.in);
    public int eleccion(){
        while(true){

            System.out.println("Que vas a elegir: ");
            int numero = sc.nextInt();
            sc.nextLine();
            return numero;
        }

    }

    public int MenuLibros(){
        System.out.println("1. Añadir libro nuevo: ");
        System.out.println("2. Ver el listado completo de todos mis libros: ");
        System.out.println("3. Buscar un libro específico por su ISBN: ");
        System.out.println("4. Buscar todos los libros de una categoría concreta: ");
        System.out.println("5. Actualizar el stock: ");
        System.out.println("6. Ver qué libros están por debajo de 5 unidades: ");
        System.out.println("7. Volver. ");

        return eleccion();
    }

    public int MenuClientes(){
        System.out.println("1. Añadir cliente nuevo: ");
        System.out.println("2. Ver el listado completo de todos mis clientes: ");
        System.out.println("3. Buscar un cliente específico por su DNI: ");
        System.out.println("4. Ver los mejores clientes : ");
        System.out.println("5. Actualizar un cliente: ");
        System.out.println("6. Volver. ");

        return eleccion();
    }



}
