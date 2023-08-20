package com.neshan.resturantrest.services;

import com.github.javafaker.Faker;
import com.neshan.resturantrest.models.Restaurant;
import com.neshan.resturantrest.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final UserService userService;

    private List<Restaurant> db = new ArrayList<>() {{
        Faker faker = new Faker();

        for (int i = 0; i <= 5; i++) {
            Restaurant restaurant = Restaurant
                    .builder()
                    .id(String.valueOf(i))
                    .ownerId(String.valueOf(i))
                    .name(faker.name().firstName())
                    .address(faker.address().fullAddress())
                    .build();

            add(restaurant);
        }
    }};

    public List<Restaurant> get() {
        return db;
    }

    public Restaurant delete(Restaurant restaurant) {
        db.remove(restaurant);

        return restaurant;
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

    public List<Restaurant> getRestaurantsByOwnerId(String ownerId) {
        return db.stream()
                .filter(restaurant -> restaurant.getOwnerId().equals(ownerId))
                .toList();
    }
}
