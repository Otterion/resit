package ru.shers.resit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import ru.shers.resit.entities.Role;
import ru.shers.resit.repository.RoleRepository;

@Service
@RequiredArgsConstructor
public class RoleService {
    public final RoleRepository roleRepository;

    public Role getUserRole() {
        return roleRepository.findByName("ROLE_USER").get();
    }
}
