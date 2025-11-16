// java
package com.techdam.DAO;
import com.techdam.Model.Empleado;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.techdam.Utiles.Utiles.*;
import static com.techdam.Utiles.utilesConexion.*;

public class EmpleadoDAO {
    // Constructor vacío de la clase DAO
    public EmpleadoDAO() {
    }

    // Metodo para insertar un empleado en la base de datos.
    // Devuelve el id generado por la BD (o 0 si no se insertó).
    public int crearEmpleado(Empleado empleado) {
        int idgenerada = 0;

        // comprobamos que el objeto empleado no sea null
        if (empleado != null) {
            // try-with-resources: cierra la conexión automáticamente
            try (Connection con = getConexion()) {
                if (con != null) {
                    // Sentencia SQL para insertar. Usamos parámetros (?) para evitar SQL injection.
                    String sentencia = "INSERT INTO empleados(nombre,departamento,salario,activo) VALUES (?,?,?,?)";

                    // Preparamos la sentencia pidiendo que retorne las keys generadas
                    PreparedStatement ps = con.prepareStatement(sentencia, Statement.RETURN_GENERATED_KEYS);

                    // seteamos los valores en el PreparedStatement según el orden de columnas
                    ps.setString(1, empleado.getNombre());
                    ps.setString(2, empleado.getDepartamento());
                    ps.setDouble(3, empleado.getSalario());
                    ps.setBoolean(4, empleado.isActivo());

                    // ejecutamos la inserción
                    int cambio = ps.executeUpdate();

                    // si se insertó al menos una fila, intentamos leer el id generado
                    if (cambio > 0) {
                        try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                            if (generatedKeys.next()) {
                                idgenerada = generatedKeys.getInt(1); // id devuelto por la BD
                            } else {
                                throw new SQLException("No se pudo obtener el ID generado.");
                            }
                        }

                        System.out.println("Empleado insertado correctamente. ID generado: " + idgenerada);
                    } else {
                        System.out.println("No se pudo insertar el empleado");
                    }
                } else {
                    System.out.println("No se pudo establecer la conexión");
                }
            } catch (SQLException e) {
                // En caso de error SQL imprimimos el mensaje (se podría loggear mejor)
                System.out.println("Error SQL: " + e.getMessage());
            }
        } else {
            // el objeto empleado era null
            System.out.println("El empleado no tiene bien los datos");
        }

        // devolvemos el id generado o 0 si algo falló
        return idgenerada;
    }



    // Método para obtener todos los empleados de la tabla.
    // Devuelve una lista (vacía si no hay o si hay error).
    public List<Empleado> obtenerEmpleados() {
        List<Empleado> lista = new ArrayList<>();

        try (Connection con = getConexion()) {
            if (con != null) {
                // Select simple para traer todas las columnas
                String sentencia = "Select * FROM empleados";
                PreparedStatement ps = con.prepareStatement(sentencia);
                ResultSet rs = ps.executeQuery();

                // Recorremos el ResultSet y creamos objetos Empleado con los datos
                while (rs.next()) {
                    lista.add(new Empleado(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("departamento"),
                            rs.getDouble("salario"),
                            rs.getBoolean("activo")
                    ));
                }
            }
        } catch (SQLException e) {
            // imprimir error SQL (se puede mejorar con logging)
            System.out.println(e.getMessage());
        }

        // devolvemos la lista (puede estar vacía)
        return lista;
    }

    // Obtener un empleado por su id. Usa Optional para indicar presencia/ausencia.
    public Optional<Empleado> obtenerEmpleadoPorID(int id) {

        Empleado empleado = null;
        try (Connection con = getConexion()) {
            if (con != null) {
                String sentencia = "Select * FROM empleados where id = ?";
                PreparedStatement st = con.prepareStatement(sentencia);
                st.setInt(1, id);
                ResultSet rs = st.executeQuery();

                // Si existe una fila, creamos el objeto Empleado y lo retornamos dentro de Optional
                if (rs.next()) {
                    empleado = new Empleado(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("departamento"),
                            rs.getDouble("salario"),
                            rs.getBoolean("activo")
                    );
                    return Optional.of(empleado);
                }
            }
            // Si no se encontró el id devolvemos empty
            return Optional.empty();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return Optional.empty();
    }


    // Actualiza un empleado existente. Devuelve true si se actualizó alguna fila.
    public boolean actualizarEmpleado(Empleado empleado) {
        if (empleado != null) {

            try (Connection con = getConexion()) {
                // ATENCIÓN: la sentencia original contiene errores SQL.
                // Esta es la sentencia del código original (incorrecta):
                // String sentencia = "UPDATE FROM empleados set nombre = ?, departamento = ?, double = ?, activo = ? where id = ? ";
                //
                // Sentencia corregida sugerida:
                // String sentencia = "UPDATE empleados SET nombre = ?, departamento = ?, salario = ?, activo = ? WHERE id = ?";

                // Usar la sentencia corregida para que la actualización funcione correctamente:
                String sentencia = "UPDATE empleados SET nombre = ?, departamento = ?, salario = ?, activo = ? WHERE id = ?";

                PreparedStatement st = con.prepareStatement(sentencia);
                st.setString(1, empleado.getNombre());
                st.setString(2, empleado.getDepartamento());
                st.setDouble(3, empleado.getSalario());
                st.setBoolean(4, empleado.isActivo());
                st.setInt(5, empleado.getId());

                // ejecutamos el update y comprobamos filas afectadas
                int cambiado = st.executeUpdate();
                if (cambiado > 0) {
                    System.out.println("Empleado cambiado correctamente");
                    return true;
                } else {
                    System.out.println("El Empleado no se ha podido cambiar");
                    return false;
                }

            } catch (SQLException e) {
                // imprimir error SQL
                System.out.println(e.getMessage());
            }
        }

        // si empleado era null o hubo excepción devolvemos false
        return false;
    }

    // Elimina un empleado por id. Devuelve true si se eliminó alguna fila.
    public boolean eliminarEmpleado(int id ) {

        try (Connection con = getConexion()) {
            String sentencia = "DELETE from empleados where id = ?";
            PreparedStatement st = con.prepareStatement(sentencia);
            st.setInt(1, id);

            int cambiado = st.executeUpdate();
            if (cambiado > 0) {
                System.out.println("Empleado eliminado correctamente");
                return true;
            } else {
                System.out.println("El Empleado no se ha podido eliminar");
                return false;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        // si hay error devolvemos false
        return false;
    }

}


