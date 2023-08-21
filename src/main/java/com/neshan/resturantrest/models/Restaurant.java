package com.neshan.resturantrest.models;

import lombok.*;

import java.util.Map;

@Data
@Builder
public class Restaurant {
    private String id;
    private String ownerId;
    private String name;
    private String address;

}
