package com.neshan.resturantrest.models;

import lombok.*;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@EqualsAndHashCode
@Builder
public class Restaurant {
    private String id;
    private String ownerId;
    private String name;
    private String address;

}
