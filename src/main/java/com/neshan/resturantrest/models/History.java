package com.neshan.resturantrest.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@EqualsAndHashCode
@Builder
public class History {
    private Food food;
    @JsonIgnore
    private String userId;
    private Restaurant restaurant;
    private Date createdAt;

}
