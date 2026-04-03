package az.beu.localeats.controllers;

import az.beu.localeats.dtos.restaurant.RestaurantCreateDto;
import az.beu.localeats.services.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('RESTAURANT')")
    public ResponseEntity<String> createRestaurant(@RequestBody RestaurantCreateDto dto, Principal principal) {
        restaurantService.createRestaurant(dto, principal.getName());
        return ResponseEntity.ok("Restaurant registration received. Awaiting admin approval.");
    }

    @GetMapping("/my-restaurants")
    @PreAuthorize("hasRole('RESTAURANT')")
    public ResponseEntity<?> getMyRestaurants(Principal principal) {
        return ResponseEntity.ok(restaurantService.getMyRestaurants(principal.getName()));
    }
}