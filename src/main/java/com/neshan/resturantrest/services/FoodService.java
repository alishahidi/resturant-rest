package com.neshan.resturantrest.services;

import com.github.javafaker.Faker;
import com.neshan.resturantrest.models.Food;
import com.neshan.resturantrest.models.Restaurant;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.naming.AuthenticationException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Setter
@Getter
public class FoodService {

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

    public Food get(String id) {
        return db.stream()
                .filter(food -> food.getId().equals(id))
                .findFirst()
                .orElseThrow();
    }

    public Food addFood(Food food, String restaurantId) {
        String id = UUID.randomUUID().toString();
        food.setRestaurantId(restaurantId);
        food.setId(id);
        db.add(food);

        return food;
    }

    public Food updateFood(Food food) {
        Food foundedFood = get(food.getId());

        if (food.getPrice() != null) {
            foundedFood.setPrice(food.getPrice());
        }
        if (food.getQuantity() != null) {
            foundedFood.setQuantity(food.getQuantity());
        }
        if (food.getName() != null) {
            foundedFood.setName(food.getName());
        }

        return foundedFood;
    }

    public Food deleteFood(Food food) {
        db.remove(food);

        return food;
    }

    public List<Food> getFoodsByRestaurantId(String restaurantId) {
        return db.stream()
                .filter(food -> food.getRestaurantId().equals(restaurantId))
                .toList();
    }

}
