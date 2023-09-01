package com.neshan.resturantrest.mapper;

import com.neshan.resturantrest.dto.FoodDto;
import com.neshan.resturantrest.model.Food;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class FoodMapper implements Function<Food, FoodDto> {
    @Override
    public FoodDto apply(Food food) {
        return FoodDto.builder()
                .id(food.getId())
                .name(food.getName())
                .quantity(food.getQuantity())
                .price(food.getPrice())
                .build();
    }
}
