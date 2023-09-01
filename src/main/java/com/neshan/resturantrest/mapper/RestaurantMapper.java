package com.neshan.resturantrest.mapper;

import com.neshan.resturantrest.dto.FoodDto;
import com.neshan.resturantrest.dto.RestaurantDto;
import com.neshan.resturantrest.model.Restaurant;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RestaurantMapper implements Function<Restaurant, RestaurantDto> {

    FoodMapper foodMapper;

    @Override
    public RestaurantDto apply(Restaurant restaurant) {
        return RestaurantDto.builder()
                .id(restaurant.getId())
                .name(restaurant.getName())
                .foods(
                        Optional.ofNullable(restaurant.getFoods())
                                .orElse(Collections.emptyList())
                                .stream()
                                .map(foodMapper)
                                .toList()
                )
                .address(restaurant.getAddress())
                .createdAt(restaurant.getCreatedAt())
                .updatedAt(restaurant.getUpdatedAt())
                .build();
    }
}
