package com.neshan.resturantrest.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.Date;

import org.locationtech.jts.geom.Point;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RestaurantDto {
    Long id;
    String name;
    String address;
    ArrayList<FoodDto> foods;
    Point location;
    Date createdAt;
    Date updatedAt;
}
