package com.neshan.resturantrest.services;

import com.github.javafaker.Faker;
import com.neshan.resturantrest.models.Restaurant;
import com.neshan.resturantrest.models.User;
import com.neshan.resturantrest.util.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final UserService userService;

    private List<Restaurant> db = new ArrayList<>();

    public List<Restaurant> get() {
        return db;
    }

    public Restaurant get(String id) {
        return db.stream()
                .filter(restaurant -> restaurant.getId().equals(id))
                .findFirst()
                .orElseThrow();
    }


    public Restaurant add(Restaurant restaurant, String userId) {
        User user = userService.get(userId);
        String id = UUID.randomUUID().toString();
        restaurant.setOwnerId(userId);
        restaurant.setId(id);
        db.add(restaurant);

        return restaurant;
    }
}
