package com.neshan.resturantrest.mapper;

import com.neshan.resturantrest.dto.FoodDto;
import com.neshan.resturantrest.dto.HistoryDto;
import com.neshan.resturantrest.dto.RestaurantDto;
import com.neshan.resturantrest.model.Food;
import com.neshan.resturantrest.model.History;
import com.neshan.resturantrest.model.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface HistoryMapper {
    HistoryMapper INSTANCE = Mappers.getMapper(HistoryMapper.class);

    HistoryDto historyToHistoryDto(History history);

    default FoodDto foodToFoodDto(Food food) {
        return Mappers.getMapper(FoodMapper.class).foodToFoodDto(food);
    }

    default RestaurantDto restaurantToRestaurantDto(Restaurant restaurant) {
        return Mappers.getMapper(RestaurantMapper.class).restaurantToRestaurantDTO(restaurant);
    }
}

