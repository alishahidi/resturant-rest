package com.neshan.resturantrest.controllers;

import com.neshan.resturantrest.models.Restaurant;
import com.neshan.resturantrest.services.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/restaurant")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    @GetMapping("/get")
    public List<Restaurant> get() {
        return restaurantService.get();
    }

    @PostMapping("/create/{id}")
    public ResponseEntity<Restaurant> create(@RequestBody Restaurant restaurant, @PathVariable String id) {
        Restaurant createdRestaurant = restaurantService.add(restaurant, id);

        return ResponseEntity.ok(createdRestaurant);
    }
}
