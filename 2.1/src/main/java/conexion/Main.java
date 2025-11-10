package conexion;
import static conexion.UtilesConexion.*;
public class Main {
    public static void main(String[] args) {
        Consultas consultas = new Consultas();
//        probarConexion();
//        saltolinea();
//        consultas.listarEmpleados();
//        saltolinea();
//        System.out.println("Que id quieres buscar:");
//        int id = sc.nextInt();
//        consultas.buscarPorId(id);
//        saltolinea();
        System.out.println("Que empleado quieres buscar:");
        int id2 = sc.nextInt();
        consultas.llamarProcedimiento(id2);
        saltolinea();
    }

}
