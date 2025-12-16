package com.example.demo.Service;

import com.example.demo.Models.Tarea;
import com.example.demo.Repository.TareaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TareaService {

@Autowired
    TareaRepository tareaRepository;
        public boolean insertarTarea(Tarea tarea){
            Tarea insertado = tareaRepository.save(tarea);
            if(insertado != null){
                return true;
            } else {
                return false;
            }
        }

        public double horasPorEmpleado(Long id){
            List<Tarea> lista = tareaRepository.findTareasByIdEmpleado(id);
            double horas = 0;
           if(lista.isEmpty()){
               System.out.println("No hay usuarios con ese ID");
           } else{
               for(Tarea tarea : lista){
                   horas += tarea.getHoras();
               }

           }
           return horas;
        }
}
