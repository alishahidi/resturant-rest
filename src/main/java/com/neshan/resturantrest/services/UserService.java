package com.neshan.resturantrest.services;

import com.github.javafaker.Faker;
import com.neshan.resturantrest.models.User;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    private List<User> db = new ArrayList<>() {{
        Faker faker = new Faker();

        for (int i = 0; i <= 5; i++) {
            User user = User
                    .builder()
                    .id(String.valueOf(i))
                    .name(faker.name().fullName())
                    .username(faker.internet().emailAddress())
                    .build();

            add(user);
        }
    }};

    public List<User> get() {
        return db;
    }

    public User get(String id) {
        return db.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElseThrow();
    }

    public User save(User user) {
        String id = UUID.randomUUID().toString();
        user.setId(id);
        db.add(user);

        return user;
    }

//    private Restaurant removeRestaurant(String restaurantId, String userId) {
//        User foundedUser = db.get(userId);
//        if (foundedUser == null) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
//        }
//        Restaurant foundedRestaurant = foundedUser.getRestaurants().get(restaurantId);
//        if (foundedRestaurant == null) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
//        }
//        foundedUser.getRestaurants().remove(restaurantId);
//
//        return foundedRestaurant;
//    }
//
//    public Restaurant addRestaurant(Restaurant restaurant, String userId) {
//        String id = UUID.randomUUID().toString();
//        User foundedUser = db.get(userId);
//        if (foundedUser == null) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
//        }
//        restaurant.setId(id);
//        foundedUser.getRestaurants().put(id, restaurant);
//
//        return restaurant;
//    }
//
//    public Food buyFood(String foodId, String userId, String restaurantId) {
//        User foundedUser = db.get(userId);
//        if (foundedUser == null) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
//        }
//        Restaurant foundedRestaurant = foundedUser.getRestaurants().get(restaurantId);
//        if (foundedRestaurant == null) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
//        }
//        Food foundedFood = foundedRestaurant.getFoods().get(foodId);
//        if (foundedFood == null) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
//        }
//        String id = UUID.randomUUID().toString();
//        foundedUser.getHistories().put(id, new History(id, foundedRestaurant, foundedFood, new Date()));
//
//        return foundedFood;
//    }

}
