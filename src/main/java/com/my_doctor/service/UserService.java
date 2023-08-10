package com.my_doctor.service;

import com.my_doctor.domain.Role;
import com.my_doctor.domain.User;
import com.my_doctor.model.UserDTO;
import com.my_doctor.repos.RoleRepository;
import com.my_doctor.repos.UserRepository;
import com.my_doctor.util.NotFoundException;
import jakarta.transaction.Transactional;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class UserService {

    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private  RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;


    public void initRoleAndUser() {

        Role adminRole = new Role();
        adminRole.setRoleName("ROLE_ADMIN");
        adminRole.setRoleDescription("Admin role");
        roleRepository.save(adminRole);

        Role userRole = new Role();
        userRole.setRoleName("ROLE_USER");
        userRole.setRoleDescription("Default role for newly created record");
        roleRepository.save(userRole);

        Role moderatorRole = new Role();
        userRole.setRoleName("ROLE_MODERATOR");
        userRole.setRoleDescription("MODERATOR Role");
        roleRepository.save(userRole);



        User adminUser = new User();
        adminUser.setUserName("admin123");
        adminUser.setPassword(encoder.encode("admin@pass"));
        adminUser.setUserFirstName("admin");
        adminUser.setUserLastName("admin");
        adminUser.setEmail("admin@gmail.com");
        Set<Role> adminRoles = new HashSet<>();
        adminRoles.add(adminRole);
        adminUser.setRoles(adminRoles);
        userRepository.save(adminUser);

    }
    public List<UserDTO> findAll() {
        final List<User> users = userRepository.findAll(Sort.by("userName"));
        return users.stream()
                .map(user -> mapToDTO(user, new UserDTO()))
                .toList();
    }

    public UserDTO get(final String userName) {
        return userRepository.findById(userName)
                .map(user -> mapToDTO(user, new UserDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public String create(final UserDTO userDTO) {
        final User user = new User();
        mapToEntity(userDTO, user);
        user.setUserName(userDTO.getUserName());
        return userRepository.save(user).getUserName();
    }

    public void update(final String userName, final UserDTO userDTO) {
        final User user = userRepository.findById(userName)
                .orElseThrow(NotFoundException::new);
        mapToEntity(userDTO, user);
        userRepository.save(user);
    }

    public void delete(final String userName) {
        userRepository.deleteById(userName);
    }

    private UserDTO mapToDTO(final User user, final UserDTO userDTO) {
        userDTO.setUserName(user.getUserName());
        userDTO.setUserFirstName(user.getUserFirstName());
        userDTO.setUserLastName(user.getUserLastName());
        userDTO.setPassword(user.getPassword());
        userDTO.setEmail(user.getEmail());
        userDTO.setEnabled(user.getEnabled());
        userDTO.setCredentialsNonExpired(user.getCredentialsNonExpired());
        userDTO.setAccountNonExpired(user.getAccountNonExpired());
        userDTO.setAccountNonLocked(user.getAccountNonLocked());
        userDTO.setRoles(user.getRoles().stream()
                .map(role -> role.getRoleName())
                .toList());
        return userDTO;
    }

    private User mapToEntity(final UserDTO userDTO, final User user) {
        user.setUserFirstName(userDTO.getUserFirstName());
        user.setUserLastName(userDTO.getUserLastName());
        user.setPassword(userDTO.getPassword());
        user.setEmail(userDTO.getEmail());
        user.setEnabled(userDTO.getEnabled());
        user.setCredentialsNonExpired(userDTO.getCredentialsNonExpired());
        user.setAccountNonExpired(userDTO.getAccountNonExpired());
        user.setAccountNonLocked(userDTO.getAccountNonLocked());
        final List<Role> roles = roleRepository.findAllById(
                userDTO.getRoles() == null ? Collections.emptyList() : userDTO.getRoles());
        if (roles.size() != (userDTO.getRoles() == null ? 0 : userDTO.getRoles().size())) {
            throw new NotFoundException("one of roles not found");
        }
        user.setRoles(roles.stream().collect(Collectors.toSet()));
        return user;
    }

    public boolean userNameExists(final String userName) {
        return userRepository.existsByUserNameIgnoreCase(userName);
    }

    public boolean emailExists(final String email) {
        return userRepository.existsByEmailIgnoreCase(email);
    }

}
