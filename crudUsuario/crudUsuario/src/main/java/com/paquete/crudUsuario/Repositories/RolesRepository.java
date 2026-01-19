package com.paquete.crudUsuario.Repositories;

import com.paquete.crudUsuario.Entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolesRepository extends JpaRepository<Roles, Long> {
}
