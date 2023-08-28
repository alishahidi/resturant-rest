package com.neshan.resturantrest.service;

import com.neshan.resturantrest.model.Food;
import com.neshan.resturantrest.model.Restaurant;
import com.neshan.resturantrest.model.User;
import com.neshan.resturantrest.repository.FoodRepository;
import com.neshan.resturantrest.repository.HistoryRepository;
import com.neshan.resturantrest.repository.RestaurantRepository;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FoodService {

    FoodRepository foodRepository;
    RestaurantRepository restaurantRepository;
    HistoryRepository historyRepository;

    public Food get(Long foodId) {
        return foodRepository.findById(foodId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "food with id: " + foodId + " dont exist.")
        );
    }

    public Food addFood(Food food, Long restaurantId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "restaurant with id: " + restaurantId + " dont exist.")
        );
        System.out.println(restaurant.getUser());
        if (!restaurant.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        food.setRestaurant(restaurant);

        Food createdFoot = foodRepository.save(food);

        return createdFoot;
    }

    public Food updateFood(Food food, Long foodId, Long restaurantId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Food foundedFood = get(foodId);
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "restaurant with id: " + restaurantId + " dont exist.")
        );

        if (!foundedFood.getRestaurant().getId().equals(restaurantId) || !restaurant.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        if (food.getPrice() != null) {
            foundedFood.setPrice(food.getPrice());
        }
        if (food.getQuantity() != null) {
            foundedFood.setQuantity(food.getQuantity());
        }
        if (food.getName() != null) {
            foundedFood.setName(food.getName());
        }

        foodRepository.save(foundedFood);
        return foundedFood;
    }

    public Food deleteFood(Long foodId, Long restaurantId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Food food = foodRepository.findById(foodId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "food with id: " + foodId + " dont exist.")
        );

        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "restaurant with id: " + restaurantId + " dont exist.")
        );

        if (!food.getRestaurant().getId().equals(restaurantId) || !restaurant.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        historyRepository.deleteHistoriesByFoodId(foodId);
        foodRepository.deleteFoodById(foodId);

        return food;
    }
}
