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

    private List<Restaurant> db = new ArrayList<>() {{
        Faker faker = new Faker();

        for (int i = 0; i <= 5; i++) {
            User user = Utils.random(userService.get());
            String id = UUID.randomUUID().toString();
            Restaurant restaurant = Restaurant
                    .builder()
                    .id(id)
                    .ownerId(user.getId())
                    .name(faker.name().firstName())
                    .address(faker.address().fullAddress())
                    .build();


            add(restaurant);
        }
    }};
}
