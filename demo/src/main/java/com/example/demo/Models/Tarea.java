package com.example.demo.Models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data@AllArgsConstructor@NoArgsConstructor
@Table(name = "tarea")
public class Tarea {
    @Id
    @Column(name = "id_tarea")
    private Long id;

    @Column(name = "id_empleado")
    private Long idEmpleado;

    @Column(name = "id_proyecto")
    private Long idProyecto;

    @Column(name = "horas")
    private int horas;

}
