package com.neshan.resturantrest.controller;

import com.neshan.resturantrest.config.TrackExecutionTime;
import com.neshan.resturantrest.entity.GetRestaurantResponse;
import com.neshan.resturantrest.model.Food;
import com.neshan.resturantrest.model.History;
import com.neshan.resturantrest.model.Restaurant;
import com.neshan.resturantrest.model.User;
import com.neshan.resturantrest.service.FoodService;
import com.neshan.resturantrest.service.HistoryService;
import com.neshan.resturantrest.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/restaurant")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final FoodService foodService;
    private final HistoryService historyService;

    @GetMapping
    @TrackExecutionTime
    public List<Restaurant> get() throws InterruptedException {

        Thread.sleep(1000);
        return restaurantService.get();
    }

    @GetMapping("/{id}")
    @TrackExecutionTime
    public ResponseEntity<Restaurant> get(@PathVariable Long id) {
        Restaurant restaurant = restaurantService.get(id);

        return ResponseEntity.ok(restaurant);
    }

    @PostMapping("/create")
    @TrackExecutionTime
    public ResponseEntity<Restaurant> create(@RequestBody Restaurant restaurant) {
        Restaurant createdRestaurant = restaurantService.add(restaurant);

        return ResponseEntity.ok(createdRestaurant);
    }

    @DeleteMapping("/{id}")
    @TrackExecutionTime
    public ResponseEntity<Restaurant> delete(@PathVariable Long id) {
        Restaurant restaurant = restaurantService.delete(id);

        return ResponseEntity.ok(restaurant);
    }

    @PostMapping("/food/{restaurantId}")
    @TrackExecutionTime
    public ResponseEntity<Food> addFood(@RequestBody Food food, @PathVariable Long restaurantId) {
        Food createdFood = foodService.addFood(food, restaurantId);

        return ResponseEntity.ok(createdFood);
    }

    @GetMapping("/food/serve/{foodId}/{restaurantId}")
    @TrackExecutionTime
    public ResponseEntity<History> serveFood(@PathVariable Long foodId, @PathVariable Long restaurantId) {
        History history = restaurantService.serveFood(foodId, restaurantId);

        return ResponseEntity.ok(history);
    }

    @PutMapping("/food/{id}/{restaurantId}")
    @TrackExecutionTime
    public ResponseEntity<Food> updateFood(@RequestBody Food food, @PathVariable Long id, @PathVariable Long restaurantId) {
        Food createdFood = foodService.updateFood(food, id, restaurantId);

        return ResponseEntity.ok(createdFood);
    }

    @DeleteMapping("/food/{id}/{restaurantId}")
    @TrackExecutionTime
    public ResponseEntity<Food> deleteFood(@PathVariable Long id, @PathVariable Long restaurantId) {
        Food deletedFood = foodService.deleteFood(id, restaurantId);

        return ResponseEntity.ok(deletedFood);
    }
}
