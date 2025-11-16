package com.techdam.Model;

public class Empleado {
    private int id;
    private String nombre;
    private String departamento;
    private double salario;
    private boolean activo;


    public Empleado(String nombre, String departamento, double salario, boolean activo) {
        this.nombre = nombre;
        this.departamento = departamento;
        this.salario = salario;
        this.activo = activo;
    }

    public Empleado(int id, String nombre, String departamento, double salario, boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.departamento = departamento;
        this.salario = salario;
        this.activo = activo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
