package com.neshan.resturantrest.controllers;

import com.neshan.resturantrest.entities.GetRestaurantResponse;
import com.neshan.resturantrest.models.Food;
import com.neshan.resturantrest.models.Restaurant;
import com.neshan.resturantrest.services.FoodService;
import com.neshan.resturantrest.services.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/restaurant")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final FoodService foodService;

    @GetMapping("/get")
    public List<Restaurant> get() {
        return restaurantService.get();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<GetRestaurantResponse> get(@PathVariable String id) {
        Restaurant restaurant = restaurantService.get(id);
        List<Food> menu = foodService.getFoodsByRestaurantId(id);

        return ResponseEntity.ok(
                GetRestaurantResponse.builder()
                        .restaurant(restaurant)
                        .menu(menu)
                        .build()
        );
    }

    @PostMapping("/create/{id}")
    public ResponseEntity<Restaurant> create(@RequestBody Restaurant restaurant, @PathVariable String id) {
        Restaurant createdRestaurant = restaurantService.add(restaurant, id);

        return ResponseEntity.ok(createdRestaurant);
    }

    @PostMapping("/food/{id}")
    public ResponseEntity<Food> addFood(@RequestBody Food food, @PathVariable String id) {
        Food createdFood = foodService.addFood(food, id);

        return ResponseEntity.ok(createdFood);
    }

}
