package az.beu.localeats.services.impl;


import az.beu.localeats.dtos.auth.AdminRegisterDto;
import az.beu.localeats.dtos.auth.RegisterDto;
import az.beu.localeats.dtos.auth.UserLoggedDto;
import az.beu.localeats.models.Role;
import lombok.RequiredArgsConstructor;
import az.beu.localeats.models.User;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import az.beu.localeats.repositories.UserRepository;
import az.beu.localeats.services.RoleService;
import az.beu.localeats.services.UserService;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    private final RoleService roleService;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void register(RegisterDto registerDto) {
        User user = modelMapper.map(registerDto, User.class);
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        List<Role> roles = new ArrayList<>();
        if ("RESTAURANT_OWNER".equals(registerDto.getRoleName())) {
            roles.add(roleService.findByName("ROLE_RESTAURANT"));
        } else {
            roles.add(roleService.findByName("ROLE_CUSTOMER"));
        }
        user.setRoles(roles);

        userRepository.save(user);
    }

    @Override
    public User findByLoggedUser(String email) {
        return userRepository.findByEmail(email).orElseThrow();
    }

    @Override
    public User findUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow();
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow();
    }

    @Override
    public UserLoggedDto getLoggedUserInfo(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        UserLoggedDto userLoggedDto = modelMapper.map(user, UserLoggedDto.class);
        String role = "user";
        userLoggedDto.setActive(true);
        userLoggedDto.setRoleName(role);
        return userLoggedDto;
    }

    @Override
    @Transactional
    public void registerAdmin(AdminRegisterDto adminRegisterDto) {
        if (userRepository.findByEmail(adminRegisterDto.getEmail()).isPresent()) {
            throw new RuntimeException("This user is already registered");
        }

        User admin = modelMapper.map(adminRegisterDto, User.class);
        admin.setPassword(passwordEncoder.encode(adminRegisterDto.getPassword()));

        Role adminRole = roleService.findByName("ROLE_ADMIN");
        admin.setRoles(Collections.singletonList(adminRole));

        userRepository.save(admin);
    }
}
