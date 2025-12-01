package com.dam.accesodatos.miprimeraapi.model;

public class Tarea {
    private int id;
    private String titulo;
    private boolean completada;



    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public boolean isCompletada() { return completada; }

    public Tarea(int id, String titulo, boolean completada) {
        this.id = id;
        this.titulo = titulo;
        this.completada = completada;
    }

    @Override
    public String toString() {
        return "Tarea{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", completada=" + completada +
                '}';
    }

}
