import java.io.*;
import java.util.InputMismatchException;
import java.util.Scanner;
import POJO.Movimiento;

public class Utiles implements Serializable {
    private static final long serialVersionUID = 1L;
    final String archivo = "datos/cuenta.dat";
    Movimiento mov = new Movimiento();
    Scanner sc = new Scanner(System.in);
    public void crearArchivos() {
        boolean creado = false;
        File carpeta = new File("datos");
        if (!carpeta.exists()) {
            creado = carpeta.mkdir();
            System.out.println("Carpeta creada");
        }
        if (!creado) {
            System.out.println("La carpeta no se ha podido crear");
        } else {
            try {
                File ar = new File(archivo);
                if (!ar.exists()) {
                    ar.createNewFile();
                    System.out.println("Archivo creado");
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    public void guardarCuenta(Cuenta cuenta){
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))) {
            oos.writeObject(cuenta);
            System.out.println("Cuenta guardada correctamente.");
        } catch (IOException e) {
            // Capturamos cualquier error de escritura
            System.out.println("Error al guardar la cuenta: " + e.getMessage());
        }
    }

    public Cuenta cargarCuenta(){

        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))){
            Cuenta cuentaCargada = (Cuenta) ois.readObject();
            System.out.println("Cuenta cargada correctamente.");
            return cuentaCargada;

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error al cargar la cuenta: " + e.getMessage());
            return new Cuenta(); // si hay error, devolvemos cuenta vacía
        }
    }

    public double pedirDouble() {
        double numero = 0;
        try {
            while (true) {
                 numero = sc.nextDouble();
                if (numero >= 0) {
                    break;
                } else {
                    System.out.println("El numero que has introducido no es valido, solo numeros positivos");
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    return numero;
    }



    public int pedirInt() {
        int num = -1;
        boolean valido = false;


        while (!valido) {
            try {
                System.out.print("Introduce un número entero positivo: ");
                num = sc.nextInt();

                if (num >= 0) {
                    valido = true;
                } else {
                    System.out.println("Número no válido. Debe ser mayor o igual a 0.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Debes ingresar un número entero.");
                sc.nextLine(); // limpia el buffer

            }
        }

        return num;
    }




    public void saltoLinea(){
        System.out.println("Pulsa espacio para continuar");
        String salto = sc.nextLine();
    }


}
