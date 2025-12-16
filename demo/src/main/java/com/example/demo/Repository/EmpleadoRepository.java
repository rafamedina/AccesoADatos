package com.example.demo.Repository;

import com.example.demo.Models.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {

    @Query(value = "SELECT * FROM empleado",nativeQuery = true)
    List<Empleado> getAll();

    @Query(value = "SELECT * FROM empleado where id_empleado = ?1", nativeQuery = true)
    Optional<Empleado> getEmpleadoById(Long id);

}
