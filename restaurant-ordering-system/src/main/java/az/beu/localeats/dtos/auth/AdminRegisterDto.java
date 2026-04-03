package az.beu.localeats.dtos.auth;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminRegisterDto {
    private String name;
    private String surname;
    private String email;
    private String password;
}