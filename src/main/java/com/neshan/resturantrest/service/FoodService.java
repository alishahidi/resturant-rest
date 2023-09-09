package com.neshan.resturantrest.service;

import com.neshan.resturantrest.dto.FoodDto;
import com.neshan.resturantrest.dto.RestaurantDto;
import com.neshan.resturantrest.mapper.FoodMapper;
import com.neshan.resturantrest.mapper.RestaurantMapper;
import com.neshan.resturantrest.model.Food;
import com.neshan.resturantrest.model.Restaurant;
import com.neshan.resturantrest.model.User;
import com.neshan.resturantrest.repository.FoodRepository;
import com.neshan.resturantrest.repository.HistoryRepository;
import com.neshan.resturantrest.repository.RestaurantRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@EnableCaching
public class FoodService {

    FoodRepository foodRepository;
    RestaurantRepository restaurantRepository;
    HistoryRepository historyRepository;
    RedissonClient redissonClient;

    public FoodDto get(Long foodId) {
        return foodRepository.findById(foodId).map(FoodMapper.INSTANCE::foodToFoodDto).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "food with id: " + foodId + " dont exist.")
        );
    }

    public FoodDto addFood(Food food, Long restaurantId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "restaurant with id: " + restaurantId + " dont exist.")
        );
        if (!restaurant.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        food.setRestaurant(restaurant);
        FoodDto foodDto = FoodMapper.INSTANCE.foodToFoodDto(foodRepository.save(food));
        RMap<Long, RestaurantDto> restaurantCache = redissonClient.getMap("restaurants");
        RestaurantDto restaurantDto = restaurantCache.get(restaurantId);
        if (restaurantDto != null) {
            restaurantDto.getFoods().add(foodDto);
            restaurantCache.put(restaurantId, restaurantDto);
        }

        return foodDto;
    }

    public FoodDto updateFood(Food food, Long foodId, Long restaurantId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Food foundedFood = foodRepository.findById(foodId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "food with id: " + foodId + " dont exist.")
        );
        if (!foodBelongsToRestaurant(food, restaurantId) || !restaurantBelongsToUser(food, user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        if (food.getPrice() != null) {
            foundedFood.setPrice(food.getPrice());
        }
        if (food.getQuantity() != null) {
            foundedFood.setQuantity(food.getQuantity());
        }
        if (food.getName() != null) {
            foundedFood.setName(food.getName());
        }
        FoodDto foodDto = FoodMapper.INSTANCE.foodToFoodDto(foodRepository.save(foundedFood));
        RMap<Long, RestaurantDto> restaurantCache = redissonClient.getMap("restaurants");
        restaurantCache.put(restaurantId, RestaurantMapper.INSTANCE.restaurantToRestaurantDTO(restaurantRepository.findById(restaurantId).get()));

        return foodDto;
    }

    @Transactional
    public FoodDto deleteFood(Long foodId, Long restaurantId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Food food = foodRepository.findById(foodId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "food with id: " + foodId + " dont exist.")
        );
        if (!foodBelongsToRestaurant(food, restaurantId) || !restaurantBelongsToUser(food, user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        historyRepository.deleteHistoriesByFoodId(foodId);
        foodRepository.deleteFoodById(foodId);

        RMap<Long, RestaurantDto> restaurantCache = redissonClient.getMap("restaurants");
        RestaurantDto restaurantDto = restaurantCache.get(restaurantId);
        if (restaurantDto != null) {
            restaurantDto.getFoods().removeIf(foodDto -> foodDto.getId().equals(foodId));
            restaurantCache.put(restaurantId, restaurantDto);
        }

        return FoodMapper.INSTANCE.foodToFoodDto(food);
    }

    private boolean foodBelongsToRestaurant(Food food, Long restaurantId){
        return food.getRestaurant().getId().equals(restaurantId);
    }

    private boolean restaurantBelongsToUser(Food food, Long userId){
        return food.getRestaurant().getUser().getId().equals(userId);
    }
}
