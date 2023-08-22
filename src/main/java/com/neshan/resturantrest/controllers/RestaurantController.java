package com.neshan.resturantrest.controllers;

import com.neshan.resturantrest.entities.GetRestaurantResponse;
import com.neshan.resturantrest.models.Food;
import com.neshan.resturantrest.models.History;
import com.neshan.resturantrest.models.Restaurant;
import com.neshan.resturantrest.services.FoodService;
import com.neshan.resturantrest.services.HistoryService;
import com.neshan.resturantrest.services.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.naming.AuthenticationException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/restaurant")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final FoodService foodService;
    private final HistoryService historyService;

    @GetMapping
    public List<Restaurant> get() {
        return restaurantService.get();
    }

    @GetMapping("/{id}")
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

    @DeleteMapping("/{id}/{userId}")
    public ResponseEntity<GetRestaurantResponse> delete(@PathVariable String id, @PathVariable String userId) {
        Restaurant restaurant = restaurantService.get(id);
        if (!restaurant.getOwnerId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        List<Food> menu = foodService.getFoodsByRestaurantId(id);
        menu.forEach(foodService::deleteFood);

        return ResponseEntity.ok(
                GetRestaurantResponse.builder()
                        .restaurant(restaurant)
                        .menu(menu)
                        .build()
        );
    }


    @PostMapping("/food/{id}")
    public ResponseEntity<Food> addFood(@RequestBody Food food, @PathVariable String id) {
        Food createdFood = foodService.addFood(food, id);

        return ResponseEntity.ok(createdFood);
    }

    @GetMapping("/food/serve/{id}/{restaurantId}/{userId}")
    public ResponseEntity<History> serveFood(@PathVariable String id, @PathVariable String restaurantId, @PathVariable String userId) {
        Food food = foodService.get(id);

        if (!food.getRestaurantId().equals(restaurantId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        if (food.getQuantity() <= 0) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "food quantity is zero");
        }
        food.setQuantity(food.getQuantity() - 1);
        Restaurant restaurant = restaurantService.get(restaurantId);

        History history = historyService.add(userId, food, restaurant);
        foodService.updateFood(food);

        return ResponseEntity.ok(history);
    }

    @PutMapping("/food/{id}/{restaurantId}/{userId}")
    public ResponseEntity<Food> updateFood(@RequestBody Food food, @PathVariable String id, @PathVariable String restaurantId, @PathVariable String userId) {
        Food foundedFood = foodService.get(id);
        Restaurant restaurant = restaurantService.get(foundedFood.getRestaurantId());
        if (!foundedFood.getRestaurantId().equals(restaurantId) || !restaurant.getOwnerId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        food.setId(id);
        food.setRestaurantId(restaurantId);

        return ResponseEntity.ok(foodService.updateFood(food));
    }

    @DeleteMapping("/food/{id}/{restaurantId}/{userId}")
    public ResponseEntity<Food> deleteFood(@PathVariable String id, @PathVariable String restaurantId, @PathVariable String userId) {
        Food food = foodService.get(id);
        Restaurant restaurant = restaurantService.get(food.getRestaurantId());
        if (!food.getRestaurantId().equals(restaurantId) || !restaurant.getOwnerId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        return ResponseEntity.ok(foodService.deleteFood(food));
    }
}
