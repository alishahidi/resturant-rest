package com.neshan.resturantrest.service;

import com.neshan.resturantrest.dto.FoodDto;
import com.neshan.resturantrest.mapper.FoodMapper;
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
    FoodMapper foodMapper;


    public FoodDto get(Long foodId) {
        return foodRepository.findById(foodId).map(foodMapper).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "food with id: " + foodId + " dont exist.")
        );
    }

    public FoodDto addFood(Food food, Long restaurantId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "restaurant with id: " + restaurantId + " dont exist.")
        );
        if (!restaurant.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        food.setRestaurant(restaurant);

        return foodMapper.apply(foodRepository.save(food));
    }

    public FoodDto updateFood(Food food, Long foodId, Long restaurantId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Food foundedFood = foodRepository.findById(foodId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "food with id: " + foodId + " dont exist.")
        );

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

        return foodMapper.apply(foodRepository.save(foundedFood));
    }

    public FoodDto deleteFood(Long foodId, Long restaurantId) {
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

        return foodMapper.apply(food);
    }
}
