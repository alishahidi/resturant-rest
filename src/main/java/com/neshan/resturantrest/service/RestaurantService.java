package com.neshan.resturantrest.service;

import com.github.javafaker.Faker;
import com.neshan.resturantrest.entity.GetRestaurantResponse;
import com.neshan.resturantrest.model.Food;
import com.neshan.resturantrest.model.History;
import com.neshan.resturantrest.model.Restaurant;
import com.neshan.resturantrest.model.User;
import com.neshan.resturantrest.repository.FoodRepository;
import com.neshan.resturantrest.repository.HistoryRepository;
import com.neshan.resturantrest.repository.RestaurantRepository;
import com.neshan.resturantrest.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RestaurantService {

    RestaurantRepository restaurantRepository;
    FoodRepository foodRepository;
    UserRepository userRepository;
    HistoryRepository historyRepository;
    HistoryService historyService;

    public List<Restaurant> get() {
        return restaurantRepository.findAll(Sort.by("createdAt").descending()); // B => s
    }

    public Restaurant delete(Long restaurantId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "restaurant with id: " + restaurantId + " dont exist.")
        );

        if (!restaurant.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Restaurant owner dont math with you.");
        }

        historyRepository.deleteHistoriesByRestaurantId(restaurantId);
        foodRepository.deleteFoodsByRestaurantId(restaurantId);
        restaurantRepository.deleteRestaurantById(restaurantId);
        return restaurant;
    }

    public Restaurant get(Long restaurantId) {
        return restaurantRepository.findById(restaurantId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "restaurant with id: " + restaurantId + " dont exist.")
        );
    }

    public Restaurant add(Restaurant restaurant) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        restaurant.setUser(user);
//        restaurant.setUser(userRepository.findById(user.getId()).get());

        Restaurant createdRestaurant = restaurantRepository.save(restaurant);

        return createdRestaurant;
    }

    public History serveFood(Long foodId, Long restaurantId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Food food = foodRepository.findById(foodId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "food with id: " + foodId + " dont exist.")
        );

        if (!food.getRestaurant().getId().equals(restaurantId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        if (food.getQuantity() <= 0) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "food quantity is zero");
        }
        food.setQuantity(food.getQuantity() - 1);

        History history = historyService.add(user.getId(), foodId, restaurantId);

        foodRepository.save(food);

        return history;
    }
}
