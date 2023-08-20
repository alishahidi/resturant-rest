package com.neshan.resturantrest.models;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    private String id;
    private String name;
    private String username;
    private Map<String, Restaurant> restaurants;
    private Map<String, History> histories;

}
