package com.dam.accesodatos.miprimeraapi.controller;

import com.dam.accesodatos.miprimeraapi.model.Tarea;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


    @RestController
    public class TareaController {

        @GetMapping("/api/tareas")
        public List<Tarea> tareas() {
            List<Tarea> tareas = new ArrayList<>();
            tareas.add(new Tarea(1, "Tarea 1", true));
            tareas.add(new Tarea(2, "Tarea 2", false));
            tareas.add(new Tarea(3, "Tarea 3", true));

            return tareas;
        }
}
