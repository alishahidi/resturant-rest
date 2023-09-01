package com.neshan.resturantrest.dto;

import com.neshan.resturantrest.model.Food;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RestaurantDto {
    Long id;
    String name;
    String address;
    List<FoodDto> foods;
    Date createdAt;
    Date updatedAt;
}
