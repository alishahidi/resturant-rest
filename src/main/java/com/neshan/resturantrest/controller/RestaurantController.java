package com.neshan.resturantrest.controller;

import com.neshan.resturantrest.config.TrackExecutionTime;
import com.neshan.resturantrest.dto.FoodDto;
import com.neshan.resturantrest.dto.HistoryDto;
import com.neshan.resturantrest.dto.RestaurantDto;
import com.neshan.resturantrest.model.Food;
import com.neshan.resturantrest.model.Restaurant;
import com.neshan.resturantrest.service.FoodService;
import com.neshan.resturantrest.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/restaurant")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final FoodService foodService;

    @GetMapping
    @TrackExecutionTime
    public List<RestaurantDto> get() throws InterruptedException {
        return restaurantService.get();
    }

    @GetMapping("/{id}")
    @TrackExecutionTime
    public ResponseEntity<RestaurantDto> get(@PathVariable Long id) {
        return ResponseEntity.ok(restaurantService.get(id));
    }

    @PostMapping("/create")
    @TrackExecutionTime
    public ResponseEntity<RestaurantDto> create(@RequestBody Restaurant restaurant) {
        return ResponseEntity.ok(restaurantService.add(restaurant));
    }

    @DeleteMapping("/{id}")
    @TrackExecutionTime
    public ResponseEntity<RestaurantDto> delete(@PathVariable Long id) {
        return ResponseEntity.ok(restaurantService.delete(id));
    }

    @PostMapping("/food/{restaurantId}")
    @TrackExecutionTime
    public ResponseEntity<FoodDto> addFood(@RequestBody Food food, @PathVariable Long restaurantId) {
        return ResponseEntity.ok(foodService.addFood(food, restaurantId));
    }

    @GetMapping("/food/serve/{foodId}/{restaurantId}")
    @TrackExecutionTime
    public ResponseEntity<HistoryDto> serveFood(@PathVariable Long foodId, @PathVariable Long restaurantId) {
        return ResponseEntity.ok(restaurantService.serveFood(foodId, restaurantId));
    }

    @PutMapping("/food/{id}/{restaurantId}")
    @TrackExecutionTime
    public ResponseEntity<FoodDto> updateFood(@RequestBody Food food, @PathVariable Long id, @PathVariable Long restaurantId) {
        return ResponseEntity.ok(foodService.updateFood(food, id, restaurantId));
    }

    @DeleteMapping("/food/{id}/{restaurantId}")
    @TrackExecutionTime
    public ResponseEntity<FoodDto> deleteFood(@PathVariable Long id, @PathVariable Long restaurantId) {
        return ResponseEntity.ok(foodService.deleteFood(id, restaurantId));
    }
}
