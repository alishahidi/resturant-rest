package com.neshan.resturantrest.services;

import com.neshan.resturantrest.models.Food;
import com.neshan.resturantrest.models.Restaurant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FoodService {

    private final RestaurantService restaurantService;

    private List<Food> db = new ArrayList<>();

    public Food addFood(Food food, String userId) throws AuthenticationException {
        String id = UUID.randomUUID().toString();
        Restaurant restaurant = restaurantService.get(food.getRestaurantId());
        if (!restaurant.getOwnerId().equals(userId)) {
            throw new AuthenticationException("User dont have restaurant");
        }
        food.setId(id);
        db.add(food);

        return food;
    }

    public List<Food> getFoodsByRestaurantId(String restaurantId) {
        return db.stream()
                .filter(food -> food.getRestaurantId().equals(restaurantId))
                .toList();
    }

}
