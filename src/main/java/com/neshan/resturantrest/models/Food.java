package com.neshan.resturantrest.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Food {
    private String id;
    private String name;
    private Double price;
    private Integer quantity;
}
