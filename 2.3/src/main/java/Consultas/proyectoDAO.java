package Consultas;

import java.sql.*;

import static Utiles.utilesConexion.*;

public class proyectoDAO {

    public void insertarProyecto() {
        try (Connection con = getConexion()) {
        System.out.println("Dime el nombre del proyecto");
        String nombre = sc.nextLine();
        System.out.println("Dime el presupuesto:");
        String presupuestoString = sc.nextLine().replace(",",".");
        double presupuesto = Double.parseDouble(presupuestoString);
            if (con != null) {
                String linea = "INSERT INTO proyectos (nombre, presupuesto) VALUES (?,?)";
                PreparedStatement ps = con.prepareStatement(linea);
                ps.setString(1, nombre);
                ps.setDouble(2, presupuesto);
                int resultado = ps.executeUpdate();
                if(resultado>0){
                    System.out.println("proyecto insertado correctamente");
                    mostrarProyectos();
                }else {
                    System.out.println("El proyecto no se ha podido insertar");
                }
            } else {
                System.out.println("Problema con la conexión");
            }

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void actualizarProyecto(){
        try(Connection con = getConexion()){
            boolean actualizadote = false;
            while(!actualizadote){
                System.out.println("Dime el ID del proyecto");
                int id = sc.nextInt();
                sc.nextLine();
                System.out.println("Que quieres cambiar el nombre o el presupuesto?");
                String actualizar = sc.nextLine();
                if(actualizar.equalsIgnoreCase("nombre")){
                    System.out.println("Dime el nuevo Nombre: ");
                    String nombreNuevo = sc.nextLine();
                    if(con !=null){
                        String linea = "UPDATE proyectos SET nombre = ? WHERE id = ?";
                        PreparedStatement st = con.prepareStatement(linea);
                        st.setString(1,nombreNuevo);
                        st.setInt(2,id);
                        int actualizado = st.executeUpdate();
                        if(actualizado>0){
                            System.out.println("Cliente actualizado correctamente");
                            mostrarProyectos();
                            actualizadote = true;
                        } else {
                            System.out.println("El cliente no se ha actualizado");
                        }
                    } else{
                        System.out.println("No se encuentra la conexion");
                    }

                } else if(actualizar.equalsIgnoreCase("presupuesto")) {
                    System.out.println("Dime el presupuesto:");
                    String presupuestoString = sc.nextLine().replace(",", ".");
                    double presupuesto = Double.parseDouble(presupuestoString);
                    if (con != null) {
                        String linea = "UPDATE proyectos SET presupuesto = ? WHERE id = ?";
                        PreparedStatement st = con.prepareStatement(linea);
                        st.setDouble(1, presupuesto);
                        st.setInt(2, id);
                        int actualizado = st.executeUpdate();
                        if (actualizado > 0) {
                            System.out.println("Cliente actualizado correctamente");
                            mostrarProyectos();
                            actualizadote = true;
                        } else {
                            System.out.println("El cliente no se ha actualizado");
                        }
                    } else {
                        System.out.println("No se encuentra la conexion");
                    }
                } else {
                    System.out.println("Opción no válida");
                }
            }
        }catch (RuntimeException | SQLException e){
            e.getMessage();
        }
    }

    public void eliminarPorId(){
        try(Connection con = getConexion()){
            System.out.println("Que id de proyecto quieres eliminar: ");
            int id = sc.nextInt();
            sc.nextLine();

            if(con != null){
                String linea = "Delete from proyectos where id = ? ";
                PreparedStatement ps = con.prepareStatement(linea);
                ps.setInt(1,id);
                int actualizado = ps.executeUpdate();
                if(actualizado>0){
                    System.out.println("Cliente actualizado correctamente");
                    mostrarProyectos();
                } else {
                    System.out.println("El cliente no existe o no se ha actualizado");
                }
            }



        } catch (RuntimeException | SQLException e){
            e.getMessage();
        }
    }

    public void mostrarProyectos(){
        try(Connection con = getConexion()){
        String linea = "SELECT * FROM proyectos";
            Statement st = con.prepareStatement(linea);
            ResultSet rs = st.executeQuery(linea );
            System.out.println("---------------Proyectos---------------");
            while (rs.next()) {
                System.out.printf("ID: %d | Nombre: %s | Presupuesto: %.2f €%n",
                        rs.getInt("id"), rs.getString("nombre"), rs.getDouble("presupuesto"));
            }

        } catch (RuntimeException | SQLException e){
        e.getMessage();
    }
    }
}
