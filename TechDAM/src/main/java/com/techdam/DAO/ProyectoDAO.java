// java
package com.techdam.DAO;

import com.techdam.Model.Proyecto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.techdam.Utiles.Utiles.*;
import static com.techdam.Utiles.utilesConexion.*;

public class ProyectoDAO {

    // Constructor vacío
    public ProyectoDAO() {
    }

    // Método para insertar un proyecto en la BD.
    // Devuelve el id generado (o 0 si algo falla).
    public int crearProyecto(Proyecto proyecto) {
        int idGenerado = 0;

        if (proyecto != null) {
            try (Connection con = getConexion()) {
                if (con != null) {
                    String sentencia = "INSERT INTO proyectos(nombre, presupuesto) VALUES (?, ?)";
                    PreparedStatement ps = con.prepareStatement(sentencia, Statement.RETURN_GENERATED_KEYS);

                    ps.setString(1, proyecto.getNombre());
                    ps.setDouble(2, proyecto.getPresupuesto());

                    int cambio = ps.executeUpdate();

                    if (cambio > 0) {
                        try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                            if (generatedKeys.next()) {
                                idGenerado = generatedKeys.getInt(1);
                            } else {
                                throw new SQLException("No se pudo obtener el ID generado.");
                            }
                        }
                        System.out.println("Proyecto insertado correctamente. ID generado: " + idGenerado);
                    } else {
                        System.out.println("No se pudo insertar el proyecto");
                    }
                } else {
                    System.out.println("No se pudo establecer la conexión");
                }
            } catch (SQLException e) {
                System.out.println("Error SQL: " + e.getMessage());
            }
        } else {
            System.out.println("El proyecto no tiene bien los datos");
        }

        return idGenerado;
    }

    // Devuelve todos los proyectos
    public List<Proyecto> obtenerProyectos() {
        List<Proyecto> lista = new ArrayList<>();

        try (Connection con = getConexion()) {
            if (con != null) {
                String sentencia = "SELECT * FROM proyectos";
                PreparedStatement ps = con.prepareStatement(sentencia);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    lista.add(new Proyecto(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getDouble("presupuesto")
                    ));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return lista;
    }

    // Obtener un proyecto por id
    public Optional<Proyecto> obtenerProyectoPorID(int id) {
        Proyecto proyecto = null;

        try (Connection con = getConexion()) {
            if (con != null) {
                String sentencia = "SELECT * FROM proyectos WHERE id = ?";
                PreparedStatement ps = con.prepareStatement(sentencia);
                ps.setInt(1, id);

                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    proyecto = new Proyecto(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getDouble("presupuesto")
                    );
                    return Optional.of(proyecto);
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return Optional.empty();
    }

    // Actualizar un proyecto existente
    public boolean actualizarProyecto(Proyecto proyecto) {
        if (proyecto != null) {
            try (Connection con = getConexion()) {
                String sentencia = "UPDATE proyectos SET nombre = ?, presupuesto = ? WHERE id = ?";

                PreparedStatement ps = con.prepareStatement(sentencia);
                ps.setString(1, proyecto.getNombre());
                ps.setDouble(2, proyecto.getPresupuesto());
                ps.setInt(3, proyecto.getId());

                int cambiado = ps.executeUpdate();

                if (cambiado > 0) {
                    System.out.println("Proyecto actualizado correctamente");
                    return true;
                } else {
                    System.out.println("El proyecto no se ha podido actualizar");
                    return false;
                }

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

        return false;
    }

    // Eliminar un proyecto por id
    public boolean eliminarProyecto(int id) {
        try (Connection con = getConexion()) {
            String sentencia = "DELETE FROM proyectos WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(sentencia);
            ps.setInt(1, id);

            int cambiado = ps.executeUpdate();

            if (cambiado > 0) {
                System.out.println("Proyecto eliminado correctamente");
                return true;
            } else {
                System.out.println("El proyecto no se ha podido eliminar");
                return false;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return false;
    }
}
