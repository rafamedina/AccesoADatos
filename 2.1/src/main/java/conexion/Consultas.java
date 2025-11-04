package conexion;
import static conexion.UtilesConexion.*;

import java.sql.*;

public class Consultas {

    private static String url;
    private static String user;
    private static String password;

    public Consultas(){
        String[] prop = llenarProperties();
        url = prop[0];
        user = prop[1];
        password = prop[2];
    }

    public void listarEmpleados(){
        try(Connection con = DriverManager.getConnection(url,user,password)) {

            String linea = "Select * from empleados";
            Statement s = con.prepareStatement(linea);
            ResultSet rs = s.executeQuery(linea);
            System.out.println("---------------Empleados---------------");
            while (rs.next()) {
                System.out.printf("ID: %d | Nombre: %s | Salario: %.2f €%n",
                        rs.getInt("id"), rs.getString("nombre"), rs.getDouble("salario"));
            }
        }catch (SQLException e) {
            System.err.println("❌ Error al conectar a la base de datos: " + e.getMessage());
        }
    }

    public void buscarPorId(int id){
        boolean encontrado = false;
        try(Connection con = DriverManager.getConnection(url,user,password)){

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
        try(Connection con = DriverManager.getConnection(url,user,password)){
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


}
