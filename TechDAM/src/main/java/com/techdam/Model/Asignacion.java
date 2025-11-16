package com.techdam.Model;

import java.sql.Date;

public class Asignacion {

    private int id;
    private int empleado_id;
    private int proyecto_id;
    private Date fecha;
    private String nombre;
    private String departamento;
    private double salario;
    private boolean activo;

    public Asignacion(int empleado_id, int proyecto_id, Date fecha, String nombre, String departamento, double salario, boolean activo) {
        this.empleado_id = empleado_id;
        this.proyecto_id = proyecto_id;
        this.fecha = fecha;
        this.nombre = nombre;
        this.departamento = departamento;
        this.salario = salario;
        this.activo = activo;
    }

    public int getEmpleado_id() {
        return empleado_id;
    }

    public void setEmpleado_id(int empleado_id) {
        this.empleado_id = empleado_id;
    }

    public int getProyecto_id() {
        return proyecto_id;
    }

    public void setProyecto_id(int proyecto_id) {
        this.proyecto_id = proyecto_id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
