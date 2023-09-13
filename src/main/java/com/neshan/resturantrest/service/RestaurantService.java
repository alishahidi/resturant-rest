package com.neshan.resturantrest.service;

import com.neshan.resturantrest.dto.HistoryDto;
import com.neshan.resturantrest.dto.RestaurantDto;
import com.neshan.resturantrest.mapper.RestaurantMapper;
import com.neshan.resturantrest.model.Food;
import com.neshan.resturantrest.model.Restaurant;
import com.neshan.resturantrest.model.User;
import com.neshan.resturantrest.repository.FoodRepository;
import com.neshan.resturantrest.repository.HistoryRepository;
import com.neshan.resturantrest.repository.RestaurantRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@EnableCaching
public class RestaurantService {

    RestaurantRepository restaurantRepository;
    FoodRepository foodRepository;
    HistoryRepository historyRepository;
    HistoryService historyService;
    RabbitTemplate rabbitTemplate;
    RedissonClient redissonClient;

    public List<RestaurantDto> get() throws InterruptedException {
        RMap<Long, RestaurantDto> restaurantCache = redissonClient.getMap("restaurants");
        List<RestaurantDto> restaurantDtoList;
        if (!restaurantCache.isEmpty()) {
            return restaurantCache.values().stream().toList();
        }
        restaurantDtoList = restaurantRepository.findAll().stream().map(RestaurantMapper.INSTANCE::restaurantToRestaurantDTO).toList();
        restaurantCache.putAll(restaurantDtoList.stream()
                .collect(Collectors.toMap(RestaurantDto::getId, Function.identity())));

        return restaurantDtoList;
    }

    @Transactional
    public RestaurantDto delete(Long restaurantId) {
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
        RMap<Long, RestaurantDto> restaurantCache = redissonClient.getMap("restaurants");
        if (!restaurantCache.isEmpty()) {
            restaurantCache.remove(restaurantId);
        }

        return RestaurantMapper.INSTANCE.restaurantToRestaurantDTO(restaurant);
    }

    public RestaurantDto get(Long restaurantId) {
        RMap<Long, RestaurantDto> restaurantCache = redissonClient.getMap("restaurants");
        if (!restaurantCache.isEmpty()) {
            RestaurantDto restaurantDto = restaurantCache.get(restaurantId);
            if (restaurantDto != null) {
                return restaurantDto;
            }
        }
        RestaurantDto restaurantDto = restaurantRepository.findById(restaurantId).map(RestaurantMapper.INSTANCE::restaurantToRestaurantDTO).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "restaurant with id: " + restaurantId + " dont exist.")
        );
        restaurantCache.put(restaurantId, restaurantDto);
        rabbitTemplate.convertAndSend("x.restaurant", "restaurant.get", restaurantDto);

        return restaurantDto;
    }

    public RestaurantDto add(Restaurant restaurant) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        restaurant.setUser(user);
        RestaurantDto restaurantDto = RestaurantMapper.INSTANCE.restaurantToRestaurantDTO(restaurantRepository.save(restaurant));
        RMap<Long, RestaurantDto> restaurantCache = redissonClient.getMap("restaurants");
        restaurantCache.put(restaurantDto.getId(), restaurantDto);
        rabbitTemplate.convertAndSend("x.restaurant", "restaurant.create", restaurantDto);
        return restaurantDto;
    }

    public HistoryDto serveFood(Long foodId, Long restaurantId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Food food = foodRepository.findById(foodId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "food with id: " + foodId + " dont exist.")
        );
        if (!foodBelongsToRestaurant(food, restaurantId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        if (food.getQuantity() <= 0) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "food quantity is zero");
        }
        food.setQuantity(food.getQuantity() - 1);
        foodRepository.save(food);
        RMap<Long, RestaurantDto> restaurantCache = redissonClient.getMap("restaurants");
        restaurantCache.put(restaurantId, RestaurantMapper.INSTANCE.restaurantToRestaurantDTO(restaurantRepository.findById(restaurantId).get()));

        return historyService.add(user.getId(), foodId, restaurantId);
    }

    private boolean foodBelongsToRestaurant(Food food, Long restaurantId) {
        return food.getRestaurant().getId().equals(restaurantId);
    }
}
