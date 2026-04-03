package az.beu.localeats.services.impl;

import az.beu.localeats.dtos.restaurant.RestaurantCreateDto;
import az.beu.localeats.models.Restaurant;
import az.beu.localeats.enums.RestaurantStatus;
import az.beu.localeats.models.User;
import az.beu.localeats.repositories.RestaurantRepository;
import az.beu.localeats.repositories.UserRepository;
import az.beu.localeats.services.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public void createRestaurant(RestaurantCreateDto dto, String ownerEmail) {
        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new RuntimeException("User can't be found"));

        Restaurant restaurant = modelMapper.map(dto, Restaurant.class);

        restaurant.setOwner(owner);
        restaurant.setStatus(RestaurantStatus.PENDING);

        restaurantRepository.save(restaurant);
    }

    @Override
    public List<Restaurant> getMyRestaurants(String ownerEmail) {
        User owner = userRepository.findByEmail(ownerEmail).orElseThrow();
        return restaurantRepository.findByOwnerId(owner.getId());
    }
}