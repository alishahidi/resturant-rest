package com.neshan.resturantrest.controllers;

import com.neshan.resturantrest.config.TrackExecutionTime;
import com.neshan.resturantrest.entities.GetRestaurantResponse;
import com.neshan.resturantrest.models.Food;
import com.neshan.resturantrest.models.History;
import com.neshan.resturantrest.models.Restaurant;
import com.neshan.resturantrest.models.User;
import com.neshan.resturantrest.services.FoodService;
import com.neshan.resturantrest.services.HistoryService;
import com.neshan.resturantrest.services.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
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
    @TrackExecutionTime
    public List<Restaurant> get() throws InterruptedException {
        Thread.sleep(1000);
        return restaurantService.get();
    }

    @GetMapping("/{id}")
    @TrackExecutionTime
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

    @PostMapping("/create")
    @TrackExecutionTime
    public ResponseEntity<Restaurant> create(@RequestBody Restaurant restaurant) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Restaurant createdRestaurant = restaurantService.add(restaurant, user.getId());

        return ResponseEntity.ok(createdRestaurant);
    }

    @DeleteMapping("/{id}")
    @TrackExecutionTime
    public ResponseEntity<GetRestaurantResponse> delete(@PathVariable String id) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Restaurant restaurant = restaurantService.get(id);
        if (!restaurant.getOwnerId().equals(user.getId())) {
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


    @PostMapping("/food/{restaurantId}")
    @TrackExecutionTime
    public ResponseEntity<Food> addFood(@RequestBody Food food, @PathVariable String restaurantId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Restaurant restaurant = restaurantService.get(restaurantId);
        if (!restaurant.getOwnerId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        Food createdFood = foodService.addFood(food, restaurantId);

        return ResponseEntity.ok(createdFood);
    }

    @GetMapping("/food/serve/{foodId}/{restaurantId}")
    @TrackExecutionTime
    public ResponseEntity<History> serveFood(@PathVariable String foodId, @PathVariable String restaurantId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Food food = foodService.get(foodId);
        if (!food.getRestaurantId().equals(restaurantId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        if (food.getQuantity() <= 0) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "food quantity is zero");
        }

        food.setQuantity(food.getQuantity() - 1);
        Restaurant restaurant = restaurantService.get(restaurantId);

        History history = historyService.add(user.getId(), food, restaurant);
        foodService.updateFood(food);

        return ResponseEntity.ok(history);
    }

    @PutMapping("/food/{id}/{restaurantId}")
    @TrackExecutionTime
    public ResponseEntity<Food> updateFood(@RequestBody Food food, @PathVariable String id, @PathVariable String restaurantId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Food foundedFood = foodService.get(id);
        Restaurant restaurant = restaurantService.get(foundedFood.getRestaurantId());
        if (!foundedFood.getRestaurantId().equals(restaurantId) || !restaurant.getOwnerId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        food.setId(id);
        food.setRestaurantId(restaurantId);

        return ResponseEntity.ok(foodService.updateFood(food));
    }

    @DeleteMapping("/food/{id}/{restaurantId}")
    @TrackExecutionTime
    public ResponseEntity<Food> deleteFood(@PathVariable String id, @PathVariable String restaurantId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Food food = foodService.get(id);
        Restaurant restaurant = restaurantService.get(food.getRestaurantId());
        if (!food.getRestaurantId().equals(restaurantId) || !restaurant.getOwnerId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        return ResponseEntity.ok(foodService.deleteFood(food));
    }
}
