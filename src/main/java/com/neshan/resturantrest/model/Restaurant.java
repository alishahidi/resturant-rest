package com.neshan.resturantrest.model;

import jakarta.persistence.Entity;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@EqualsAndHashCode
@Builder
@Entity
public class Restaurant {
    private String id;
    private String ownerId;
    private String name;
    private String address;

}
