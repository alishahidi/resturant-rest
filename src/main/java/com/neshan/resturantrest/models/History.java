package com.neshan.resturantrest.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class History {
    private String id;
    private Restaurant restaurant;
    private Food food;
    private Date createdAt;
}
