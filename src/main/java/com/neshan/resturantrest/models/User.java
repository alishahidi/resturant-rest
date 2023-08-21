package com.neshan.resturantrest.models;

import lombok.*;

import java.util.Map;

@Data
@Builder
public class User {
    private String id;
    private String name;
    private String username;

}
