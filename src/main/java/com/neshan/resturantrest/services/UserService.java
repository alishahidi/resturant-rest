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

    public Optional<User> findUserByUsername(String username) {
        return db.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }
}
