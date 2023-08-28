package com.neshan.resturantrest.entity;

import com.neshan.resturantrest.model.Food;
import com.neshan.resturantrest.model.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetRestaurantResponse {
    private Restaurant restaurant;
    private List<Food> menu;

}
