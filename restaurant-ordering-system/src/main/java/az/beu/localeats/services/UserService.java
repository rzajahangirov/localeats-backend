package az.beu.localeats.services;


import az.beu.localeats.dtos.auth.AdminRegisterDto;
import az.beu.localeats.dtos.auth.RegisterDto;
import az.beu.localeats.dtos.auth.UserLoggedDto;
import az.beu.localeats.models.User;

public interface UserService {
    void register(RegisterDto registerDto);
    User findByLoggedUser(String email);
    User findUserById(Long userId);
    User findUserByEmail(String email);
    UserLoggedDto getLoggedUserInfo(String email);

    void registerAdmin(AdminRegisterDto adminRegisterDto);


}