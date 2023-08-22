package com.neshan.resturantrest.controllers;

import com.neshan.resturantrest.entities.GetUserResponse;
import com.neshan.resturantrest.models.History;
import com.neshan.resturantrest.models.Restaurant;
import com.neshan.resturantrest.models.User;
import com.neshan.resturantrest.services.HistoryService;
import com.neshan.resturantrest.services.RestaurantService;
import com.neshan.resturantrest.services.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    UserService userService;
    RestaurantService restaurantService;
    HistoryService historyService;

    @GetMapping("/get")
    public ResponseEntity<GetUserResponse> getUser() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        List<Restaurant> restaurants = restaurantService.getRestaurantsByOwnerId(user.getId());
        List<History> histories = historyService.getHistoryByUserId(user.getId());

        return ResponseEntity.ok(
                GetUserResponse.builder()
                        .user(user)
                        .restaurants(restaurants)
                        .histories(histories)
                        .build()
        );
    }
}
