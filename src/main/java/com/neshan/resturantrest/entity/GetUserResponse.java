package com.neshan.resturantrest.entity;

import com.neshan.resturantrest.model.History;
import com.neshan.resturantrest.model.Restaurant;
import com.neshan.resturantrest.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetUserResponse {
    private User user;
    private List<Restaurant> restaurants;
    private List<History> histories;

}
