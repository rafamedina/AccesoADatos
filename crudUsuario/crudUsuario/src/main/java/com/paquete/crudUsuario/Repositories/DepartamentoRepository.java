package com.paquete.crudUsuario.Repositories;

import com.paquete.crudUsuario.Entity.Departamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepartamentoRepository extends JpaRepository<Departamento, Long> {

    Optional<Departamento> findDepartamentoByNombreDepartamento(String nombreDepartamento);
}
