package com.neshan.resturantrest.mapper;

import com.neshan.resturantrest.dto.FoodDto;
import com.neshan.resturantrest.model.Food;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FoodMapper {
    FoodMapper INSTANCE = Mappers.getMapper( FoodMapper.class );

    FoodDto foodToFoodDto(Food food);
}
