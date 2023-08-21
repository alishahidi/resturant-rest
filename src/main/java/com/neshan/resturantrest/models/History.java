package com.neshan.resturantrest.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class History {
    private Food food;
    @JsonIgnore
    private String userId;
    private Restaurant restaurant;
    private Date createdAt;

}
