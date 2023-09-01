package com.neshan.resturantrest.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HistoryDto {
    Long id;
    FoodDto food;
    RestaurantDto restaurant;
}
