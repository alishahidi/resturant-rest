package com.neshan.resturantrest.services;

import com.github.javafaker.Faker;
import com.neshan.resturantrest.models.Food;
import com.neshan.resturantrest.models.History;
import com.neshan.resturantrest.models.Restaurant;
import com.neshan.resturantrest.models.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

public class UserService {
    private Map<String, User> db = new HashMap<>() {{
        Faker faker = new Faker();

        for (int i = 0; i <= 5; i++) {
            String id = UUID.randomUUID().toString();
            User user = User
                    .builder()
                    .id(id)
                    .name(faker.name().fullName())
                    .username(faker.internet().emailAddress())
                    .restaurants(new HashMap<>())
                    .histories(new HashMap<>())
                    .build();

            db.put(id, user);
        }
    }};

    public User get(String id) {
        return db.get(id);
    }

    public User save(User user) {
        String id = UUID.randomUUID().toString();
        user.setId(id);
        user.setRestaurants(new HashMap<>());
        user.setHistories(new HashMap<>());
        db.put(id, user);

        return user;
    }

    private Restaurant removeRestaurant(Restaurant restaurant, String userId) {
        User foundedUser = db.get(userId);
        if (foundedUser == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        Restaurant foundedRestaurant = foundedUser.getRestaurants().get(restaurant.getId());
        if (foundedRestaurant == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        foundedUser.getRestaurants().remove(restaurant.getId());

        return restaurant;
    }

    public Restaurant addRestaurant(Restaurant restaurant, String userId) {
        String id = UUID.randomUUID().toString();
        User foundedUser = db.get(userId);
        if (foundedUser == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        restaurant.setId(id);
        foundedUser.getRestaurants().put(id, restaurant);

        return restaurant;
    }

    public Food buyFood(Food food, String userId, String restaurantId) {
        User foundedUser = db.get(userId);
        if (foundedUser == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        Restaurant foundedRestaurant = foundedUser.getRestaurants().get(restaurantId);
        if (foundedRestaurant == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        Food foundedFood = foundedRestaurant.getFoods().get(food.getId());
        if (foundedFood == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        String id = UUID.randomUUID().toString();
        foundedUser.getHistories().put(id, new History(id, foundedRestaurant, foundedFood, new Date()));

        return food;
    }

}
