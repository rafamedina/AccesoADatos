package com.paquete.crudUsuario.Services;

import com.paquete.crudUsuario.Entity.Roles;
import com.paquete.crudUsuario.Repositories.RolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RolesService {

    @Autowired
    RolesRepository rolesRepository;

    public Optional<Roles> buscarPorNombre(String nombre){



        return rolesRepository.findByNombreRol(nombre);

    }

}
