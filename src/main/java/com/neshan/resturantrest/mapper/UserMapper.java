package com.neshan.resturantrest.mapper;

import com.neshan.resturantrest.dto.FoodDto;
import com.neshan.resturantrest.dto.HistoryDto;
import com.neshan.resturantrest.dto.RestaurantDto;
import com.neshan.resturantrest.dto.UserDto;
import com.neshan.resturantrest.model.Food;
import com.neshan.resturantrest.model.History;
import com.neshan.resturantrest.model.Restaurant;
import com.neshan.resturantrest.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto userToUserDTO(User user);

    default RestaurantDto restaurantToRestaurantDto(Restaurant restaurant) {
        return Mappers.getMapper(RestaurantMapper.class).restaurantToRestaurantDTO(restaurant);
    }

    default HistoryDto historyToHistoryDto(History history) {
        return Mappers.getMapper(HistoryMapper.class).historyToHistoryDto(history);
    }
}

