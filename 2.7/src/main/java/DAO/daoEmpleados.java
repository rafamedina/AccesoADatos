package DAO;

import java.sql.*;

import static Utiles.utilesConexion.getConexion;
import static Utiles.utilesConexion.sc;

public class daoEmpleados {
    public void mostrarEmpleados(){
        try(Connection con = getConexion()){
            String linea = "SELECT * FROM empleados";
            Statement st = con.prepareStatement(linea);
            ResultSet rs = st.executeQuery(linea );
            System.out.println("---------------Proyectos---------------");
            while (rs.next()) {
                System.out.printf("ID: %d | Nombre: %s | Salario: %.2f €%n",
                        rs.getInt("id"), rs.getString("nombre"), rs.getDouble("salario"));
            }

        } catch (RuntimeException | SQLException e){
            e.getMessage();
        }
    }

    public void insertarEmpleado() {
        try (Connection con = getConexion()) {
            System.out.println("Dime el nombre del empleado");
            String nombre = sc.nextLine();
            System.out.println("Dime el salario:");
            String presupuestoString = sc.nextLine().replace(",",".");
            double salario = Double.parseDouble(presupuestoString);
            if (con != null) {
                String linea = "INSERT INTO empleados (nombre, salario) VALUES (?,?)";
                PreparedStatement ps = con.prepareStatement(linea);
                ps.setString(1, nombre);
                ps.setDouble(2, salario);
                int resultado = ps.executeUpdate();
                if(resultado>0){
                    System.out.println("empleado insertado correctamente");
                    mostrarEmpleados();
                }else {
                    System.out.println("El empleado no se ha podido insertar");
                }
            } else {
                System.out.println("Problema con la conexión");
            }

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
