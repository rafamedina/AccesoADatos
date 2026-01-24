package com.paquete.crudUsuario.Services;

import com.paquete.crudUsuario.Entity.Departamento;
import com.paquete.crudUsuario.Repositories.DepartamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DepartamentoService {

    @Autowired
    DepartamentoRepository departamentoRepository;

    public Optional<Departamento> buscarDepartamentoPorNombre(String nombre){

        return departamentoRepository.findDepartamentoByNombreDepartamento(nombre);

    }


    public List<Departamento> buscarTodos(){
        return departamentoRepository.findAll();
    }

}
