package az.beu.localeats.dtos.restaurant;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantCreateDto {
    private String name;
    private String description;
    private String cuisineType;
    private String phoneNumber;
    private String contactEmail;
    private String address;
    private String openingHours;
    private String coverImage;
}
