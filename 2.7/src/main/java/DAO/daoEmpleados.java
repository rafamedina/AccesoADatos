package DAO;

import java.sql.*;
import static Utiles.Utiles.*;

import static Utiles.utilesConexion.*;

public class daoEmpleados {
    public void mostrarEmpleados(){
        try(Connection con = getConexion()){
            String linea = "SELECT * FROM empleados";
            Statement st = con.prepareStatement(linea);
            ResultSet rs = st.executeQuery(linea );
            System.out.println("---------------Empleados---------------");
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

    public void transcacion() {
        try (Connection con = getConexion()) {
            daoProyectos dP = new daoProyectos();

            mostrarEmpleados();
            int idEmpleado = pedirInt("Que empleado quieres subirle el sueldo");

            saltolinea();

            dP.mostrarProyectos();
            int idProyecto = pedirInt("Que Proyecto quieres quitarle presupuesto");

            saltolinea();

            double cantidad = pedirDouble("Que cantidad quieres mover del proyecto al salario del empleado?");

            saltolinea();


            // Desactiva el auto-commit para manejar la transacción manualmente
            con.setAutoCommit(false);

            // PreparedStatement para retirar dinero: resta saldo a la cuenta indicada
            PreparedStatement retirarPresupuesto = con.prepareStatement("UPDATE proyectos SET presupuesto = presupuesto - ? WHERE id = ?");
            // PreparedStatement para ingresar dinero: suma saldo a la cuenta indicada
            PreparedStatement ingresarSalario = con.prepareStatement("UPDATE empleados SET salario = salario + ? WHERE id = ?");

            // Configura el primer parámetro (cantidad) y el segundo (id) para la primera actualización
            retirarPresupuesto.setDouble(1, cantidad);
            retirarPresupuesto.setInt(2, idProyecto);
            // Ejecuta la actualización que retira dinero de la cuenta 1
            retirarPresupuesto.executeUpdate();

            // Configura los parámetros para la segunda actualización
            ingresarSalario.setDouble(1, cantidad);
            ingresarSalario.setInt(2, idEmpleado);
            // Ejecuta la actualización que ingresa dinero en la cuenta 2
            ingresarSalario.executeUpdate();

            // Confirma la transacción: ambas actualizaciones se hacen permanentes
            con.commit();
            System.out.println("Transacción realizada con éxito.");

        } catch (Exception e) {
            // Si ocurre cualquier excepción, imprime la traza
            e.printStackTrace();
            try {
                // Nota: aquí se llama a ConexionBD.getConexion().rollback()
                // Esto obtiene una nueva conexión y llama rollback en ella, lo cual no revierte
                // la transacción de la conexión original. Se mantiene la lógica original sin cambiarla.
                getConexion().rollback();
            } catch (Exception ex) {
                // Imprime la traza si el rollback falla
                ex.printStackTrace();
            }
        }
    }
}
