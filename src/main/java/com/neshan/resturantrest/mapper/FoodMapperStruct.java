package com.neshan.resturantrest.mapper;

import com.neshan.resturantrest.dto.FoodDto;
import com.neshan.resturantrest.model.Food;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FoodMapperStruct {
    FoodMapperStruct INSTANCE = Mappers.getMapper( FoodMapperStruct.class );

    FoodDto foodToFoodDto(Food food);
}
