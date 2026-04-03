package az.beu.localeats.services;

import az.beu.localeats.dtos.restaurant.RestaurantCreateDto;
import az.beu.localeats.models.Restaurant;

import java.util.List;

public interface RestaurantService {
    void createRestaurant(RestaurantCreateDto dto, String ownerEmail);
    List<Restaurant> getMyRestaurants(String ownerEmail);
}