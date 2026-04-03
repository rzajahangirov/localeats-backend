package az.beu.localeats.controllers;

import az.beu.localeats.dtos.auth.*;
import az.beu.localeats.models.RefreshToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import az.beu.localeats.security.JwtService;
import az.beu.localeats.services.RefreshTokenService;
import az.beu.localeats.services.UserService;

import java.security.Principal;

@RestController
@RequestMapping("api/auth")
public class AuthController {


    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager  authenticationManager;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private JwtService jwtService;


    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserLoggedDto> me(Principal principal){
        String email = principal.getName();
        UserLoggedDto userLoggedDto = userService.getLoggedUserInfo(email);
        return new ResponseEntity<>(userLoggedDto, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterDto registerDto){
        userService.register(registerDto);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @PostMapping("/admin/register")
    public ResponseEntity<String> registerAdmin(@RequestBody AdminRegisterDto adminRegisterDto) {
        userService.registerAdmin(adminRegisterDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Admin uğurla qeydiyyatdan keçdi.");
    }



    @PostMapping("/login")
    public JwtResponseDto AuthenticateAndGetToken(@RequestBody LoginDto authRequestDTO){
        UserLoggedDto userLoggedDto =  userService.getLoggedUserInfo(authRequestDTO.getEmail());
        if (!userLoggedDto.isActive()){
            throw new UsernameNotFoundException("invalid user request..!!");
        }

//        refreshTokenService.removeToken(authRequestDTO.getEmail());
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequestDTO.getEmail(), authRequestDTO.getPassword()));
        if(authentication.isAuthenticated()){
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(authRequestDTO.getEmail());
            return JwtResponseDto.builder()
                    .accessToken(jwtService.GenerateToken(authRequestDTO.getEmail(),null))
                    .token(refreshToken.getToken())
                    .build();

        } else {
            throw new UsernameNotFoundException("invalid user request..!!");
        }
    }

    @PostMapping("/refreshToken")
    public JwtResponseDto refreshToken(@RequestBody RefreshTokenRequestDto refreshTokenRequestDTO){
        return refreshTokenService.findByToken(refreshTokenRequestDTO.getToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(userInfo -> {
                    String accessToken = jwtService.GenerateToken(userInfo.getName(),null);
                    return JwtResponseDto.builder()
                            .accessToken(accessToken)
                            .token(refreshTokenRequestDTO.getToken()).build();
                }).orElseThrow(() ->new RuntimeException("Refresh Token is not in DB..!!"));
    }



}