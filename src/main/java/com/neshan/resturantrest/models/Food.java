package com.neshan.resturantrest.models;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@EqualsAndHashCode
@Builder
public class Food {
    private String id;
    private String name;
    private Double price;
    private String restaurantId;
    private Integer quantity;

}
