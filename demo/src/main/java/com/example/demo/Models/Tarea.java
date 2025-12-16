package com.example.demo.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data@AllArgsConstructor@NoArgsConstructor
@Table(name = "tarea")
public class Tarea {
    @Id
    @Column(name = "id_tarea")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_empleado")
    private Long idEmpleado;

    @Column(name = "id_proyecto")
    private Long idProyecto;

    @Column(name = "horas")
    private double horas;

    public Tarea(Long idEmpleado, Long idProyecto, double horas) {
        this.idEmpleado = idEmpleado;
        this.idProyecto = idProyecto;
        this.horas = horas;
    }
}
