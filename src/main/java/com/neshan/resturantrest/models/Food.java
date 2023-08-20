package com.neshan.resturantrest.models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class Food {
    private String id;
    private String name;
    private Double price;
    private String restaurantId;
    private Integer quantity;

}
