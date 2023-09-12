package com.neshan.resturantrest.mapper;

import com.neshan.resturantrest.dto.FoodDto;
import com.neshan.resturantrest.dto.RestaurantDto;
import com.neshan.resturantrest.model.Food;
import com.neshan.resturantrest.model.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RestaurantMapper {
    RestaurantMapper INSTANCE = Mappers.getMapper(RestaurantMapper.class);

    RestaurantDto restaurantToRestaurantDTO(Restaurant restaurant);

    default FoodDto foodToFoodDto(Food food) {
        return Mappers.getMapper(FoodMapper.class).foodToFoodDto(food);
    }
}
