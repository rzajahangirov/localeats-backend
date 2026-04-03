package az.beu.localeats.services;


import az.beu.localeats.models.Role;

public interface RoleService {
    Role getRoleByName(String roleName);

    Role findByName(String roleRestaurant);
}