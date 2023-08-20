package com.neshan.resturantrest.controllers;

import com.neshan.resturantrest.entities.GetUserResponse;
import com.neshan.resturantrest.models.Restaurant;
import com.neshan.resturantrest.models.User;
import com.neshan.resturantrest.services.RestaurantService;
import com.neshan.resturantrest.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final RestaurantService restaurantService;

    @GetMapping("/get")
    public List<User> get() {
        return userService.get();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<GetUserResponse> getUser(@PathVariable String id) {
        User user = userService.get(id);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        List<Restaurant> ownedRestaurants = restaurantService.getRestaurantsByOwnerId(id);

        return ResponseEntity.ok(
                GetUserResponse.builder()
                        .user(user)
                        .ownedRestaurants(ownedRestaurants)
                        .build()
        );
    }
}
