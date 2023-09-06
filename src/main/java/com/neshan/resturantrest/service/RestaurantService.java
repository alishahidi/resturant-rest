package com.neshan.resturantrest.service;

import com.neshan.resturantrest.dto.HistoryDto;
import com.neshan.resturantrest.dto.RestaurantDto;
import com.neshan.resturantrest.dto.RestaurantsDto;
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
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
//    ObjectMapper objectMapper;

    @Cacheable(value = "restaurants")
    public RestaurantsDto get() throws InterruptedException {
        return RestaurantsDto.builder()
                .restaurants(restaurantRepository.findAll().stream().map(RestaurantMapper.INSTANCE::restaurantToRestaurantDTO).toList())
                .build();
    }

    @CacheEvict(value = "restaurants", key = "#restaurantId")
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

        return RestaurantMapper.INSTANCE.restaurantToRestaurantDTO(restaurant);
    }

//    @Cacheable(value = "restaurants", key = "#restaurantId")
    public RestaurantDto get(Long restaurantId) {
        RestaurantDto restaurantDto = restaurantRepository.findById(restaurantId).map(RestaurantMapper.INSTANCE::restaurantToRestaurantDTO).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "restaurant with id: " + restaurantId + " dont exist.")
        );
        rabbitTemplate.convertAndSend("x.restaurant", "restaurant.get", restaurantDto);

        return restaurantDto;
    }

//    @CacheEvict(value = "restaurants", allEntries = true)
    public RestaurantDto add(Restaurant restaurant) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        restaurant.setUser(user);
        RestaurantDto restaurantDto = RestaurantMapper.INSTANCE.restaurantToRestaurantDTO(restaurantRepository.save(restaurant));
//            Message message = MessageBuilder.withBody(objectMapper.writeValueAsBytes(restaurantDto))
//                    .setContentType(MessageProperties.CONTENT_TYPE_JSON)
//                    .build();
        rabbitTemplate.convertAndSend("x.restaurant", "restaurant.create", restaurantDto);
        return restaurantDto;
    }

    @CacheEvict(value = "restaurants", allEntries = true)
    public HistoryDto serveFood(Long foodId, Long restaurantId) {
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

        foodRepository.save(food);

        return historyService.add(user.getId(), foodId, restaurantId);
    }
}
