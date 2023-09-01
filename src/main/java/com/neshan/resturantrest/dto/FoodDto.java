package com.neshan.resturantrest.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FoodDto {
    Long id;
    String name;
    Double price;
    Integer quantity;
}
