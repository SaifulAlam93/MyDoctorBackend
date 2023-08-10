package com.my_doctor.repos;


import java.util.List;

import com.my_doctor.domain.Role;
import com.my_doctor.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, String> {

    boolean existsByUserNameIgnoreCase(String userName);

    boolean existsByEmailIgnoreCase(String email);

    List<User> findAllByRoles(Role role);

}
