package com.neshan.resturantrest.services;

import com.github.javafaker.Faker;
import com.neshan.resturantrest.models.Food;
import com.neshan.resturantrest.models.Restaurant;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.naming.AuthenticationException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FoodService {

    private final RestaurantService restaurantService;

    private List<Food> db = new ArrayList<>() {{
        Faker faker = new Faker();

        for (int i = 0; i <= 5; i++) {
            Food food = Food
                    .builder()
                    .id(String.valueOf(i))
                    .restaurantId(String.valueOf(i))
                    .name(faker.food().dish())
                    .price(faker.number().randomDouble(3, 10, 600))
                    .quantity(faker.number().numberBetween(0, 30))
                    .build();

            add(food);
        }
    }};

    public Food addFood(Food food, String userId) {
        String id = UUID.randomUUID().toString();
        Restaurant restaurant = restaurantService.get(food.getRestaurantId());
        if (!restaurant.getOwnerId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
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
