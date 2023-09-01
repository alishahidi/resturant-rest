package com.neshan.resturantrest.mapper;

import com.neshan.resturantrest.dto.HistoryDto;
import com.neshan.resturantrest.model.History;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HistoryMapper implements Function<History, HistoryDto> {

    RestaurantMapper restaurantMapper;
    FoodMapper foodMapper;

    @Override
    public HistoryDto apply(History history) {
        return HistoryDto.builder()
                .restaurant(restaurantMapper.apply(history.getRestaurant()))
                .food(foodMapper.apply(history.getFood()))
                .build();
    }
}
