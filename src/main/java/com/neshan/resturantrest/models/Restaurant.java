package com.neshan.resturantrest.models;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Restaurant {
    private String id;
    private String ownerId;
    private String name;
    private String address;

}
