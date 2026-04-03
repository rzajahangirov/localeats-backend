package az.beu.localeats.services.impl;

import az.beu.localeats.models.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import az.beu.localeats.repositories.RoleRepository;
import az.beu.localeats.services.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Role getRoleByName(String roleName) {
        Role role =roleRepository.findByName(roleName).orElseThrow();
        return role;
    }

    @Override
    public Role findByName(String roleRestaurant) {
        return roleRepository.findByName(roleRestaurant).orElseThrow();
    }
}