package DAO;

import java.sql.*;

import static Utiles.utilesConexion.getConexion;

public class clientesDAO {

    public void listarEmpleados() {
        String sql = "SELECT * FROM empleados";

        try (Connection con = getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            ResultSetMetaData rsm = rs.getMetaData();
            DatabaseMetaData dmd = con.getMetaData();
             System.out.println("--------------- Empleados ---------------");
            while (rs.next()) {
                System.out.printf("ID: %d | Nombre: %s | Salario: %.2f €%n",
                        rs.getInt("id"), rs.getString("nombre"), rs.getDouble("salario"));
            }

            System.out.println("\nNúmero de columnas: " + rsm.getColumnCount());
            for (int i = 1; i <= rsm.getColumnCount(); i++) {
                System.out.println("Columna " + i + ": " + rsm.getColumnName(i) +
                        " (" + rsm.getColumnClassName(i) + ")");
            }

            System.out.println("Nombre del producto de base de datos: " + dmd.getDatabaseProductName());
            System.out.println("Versión del motor: " + dmd.getDatabaseProductVersion());
            System.out.println("Nombre del driver JDBC: " + dmd.getDriverName());

        } catch (SQLException e) {
            System.err.println("Error al conectar a la base de datos: " + e.getMessage());
        }
    }


    public void buscarPorId(int id){
        boolean encontrado = false;
        try(Connection con = getConexion()){

            String Consulta = "Select * from empleados where id = ?";
            PreparedStatement ps = con.prepareStatement(Consulta);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {

                System.out.println("---------------Empleado " + rs.getString("nombre") + "---------------");
                System.out.printf("ID: %d | Nombre: %s | Salario: %.2f €%n",
                        rs.getInt("id"), rs.getString("nombre"), rs.getDouble("salario"));
                encontrado = true;
            }
        } catch(SQLException e){

            System.out.println(e.getMessage());
        }
        if(!encontrado){
            System.out.println("El id no existe en nuestra base de datos.");
        }
    }


    public void llamarProcedimiento(int id){
        boolean encontrado = false;
        try(Connection con = getConexion()){
            CallableStatement cs = con.prepareCall("{call obtener_empleado(?)}");
            cs.setInt(1, id);
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                System.out.println("---------------Empleado " + rs.getString("nombre") + "---------------");
                System.out.printf("ID: %d | Nombre: %s\n",
                        rs.getInt("id"), rs.getString("nombre"));
                encontrado = true;
            }
        } catch(SQLException e){

            System.out.println(e.getMessage());
        }
        if(!encontrado){
            System.out.println("El id no existe en nuestra base de datos.");
        }
    }


    public void incrementar_salario(int id, double incremento) {
        boolean encontrado = false;
        try (Connection con = getConexion()) {
            // Llamada con tres parámetros: id, incremento y salida
            CallableStatement cs = con.prepareCall("{CALL incrementar_salario(?, ?, ?)}");

            // Parámetros de entrada
            cs.setInt(1, id);
            cs.setDouble(2, incremento);

            // Parámetro de salida
            cs.registerOutParameter(3, Types.DOUBLE);

            // Ejecutar el procedimiento
            cs.execute();

            // Obtener el nuevo salario
            double nuevoSalario = cs.getDouble(3);

            // Ahora podemos mostrar los datos del empleado
            PreparedStatement ps = con.prepareStatement("SELECT id, nombre FROM empleados WHERE id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("---------------Empleado " + rs.getString("nombre") + "---------------");
                System.out.printf("ID: %d | Nombre: %s | Nuevo salario: %.2f\n",
                        rs.getInt("id"), rs.getString("nombre"), nuevoSalario);
                encontrado = true;
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        if (!encontrado) {
            System.out.println("El id no existe en nuestra base de datos.");
        }
    }



}
