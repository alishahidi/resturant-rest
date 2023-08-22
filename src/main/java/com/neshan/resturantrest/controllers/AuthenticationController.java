package com.neshan.resturantrest.controllers;

import com.neshan.resturantrest.entities.AuthenticationResponse;
import com.neshan.resturantrest.entities.GetUserResponse;
import com.neshan.resturantrest.models.History;
import com.neshan.resturantrest.models.Restaurant;
import com.neshan.resturantrest.models.User;
import com.neshan.resturantrest.requests.AuthenticationRequest;
import com.neshan.resturantrest.requests.RegisterRequest;
import com.neshan.resturantrest.services.AuthenticationService;
import com.neshan.resturantrest.services.HistoryService;
import com.neshan.resturantrest.services.RestaurantService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {

    AuthenticationService authenticationService;
    RestaurantService restaurantService;
    HistoryService historyService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @GetMapping
    public ResponseEntity<GetUserResponse> get() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

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
