package com.example.demo.Service;

import com.example.demo.Models.Empleado;
import com.example.demo.Repository.EmpleadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmpleadoService {

    @Autowired
    EmpleadoRepository empleadoRepository;

    public Optional<Empleado> buscarEmpleadoPorId(Long id){

        Optional<Empleado> empleado = empleadoRepository.getEmpleadoById(id);

        return empleado;

    }
}
