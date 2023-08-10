package com.my_doctor.repos;

import com.my_doctor.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoleRepository extends JpaRepository<Role, String> {

    boolean existsByRoleNameIgnoreCase(String roleName);

}
