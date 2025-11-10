package PoolConexiones;

import java.sql.*;
import static PoolConexiones.ConexionPool.*;
public class PruebaPool {


    public static void main(String[] args) {
        // Creación del DataSource (pool)

        // Uso de la conexión
        try (
                Connection con1 = getConexion();
                Connection con2 = getConexion();
                Connection con3 = getConexion()
        ) {
            if (con1 != null) {
                System.out.println("Conexión obtenida del pool 1");
                String linea = "Select * from empleados";
                Statement s = con1.prepareStatement(linea);
                ResultSet rs = s.executeQuery(linea);
                System.out.println("---------------Empleados---------------");
                while (rs.next()) {
                    System.out.printf("ID: %d | Nombre: %s | Salario: %.2f €%n",
                            rs.getInt("id"), rs.getString("nombre"), rs.getDouble("salario"));
                }
            }
            if (con2 != null) {
                System.out.println("Conexión obtenida del pool 2");
                String linea = "Select * from empleados";
                Statement s = con2.prepareStatement(linea);
                ResultSet rs = s.executeQuery(linea);
                System.out.println("---------------Empleados---------------");
                while (rs.next()) {
                    System.out.printf("ID: %d | Nombre: %s | Salario: %.2f €%n",
                            rs.getInt("id"), rs.getString("nombre"), rs.getDouble("salario"));
                }
            }
            if (con3 != null) {
                System.out.println("Conexión obtenida del pool 3");
                String linea = "Select * from empleados";
                Statement s = con3.prepareStatement(linea);
                ResultSet rs = s.executeQuery(linea);
                System.out.println("---------------Empleados---------------");
                while (rs.next()) {
                    System.out.printf("ID: %d | Nombre: %s | Salario: %.2f €%n",
                            rs.getInt("id"), rs.getString("nombre"), rs.getDouble("salario"));
                }
            }

            // Aquí puedes usar con1, con2 y con3
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cerrarPool();
        }

    }
    }



