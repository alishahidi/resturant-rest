package com.neshan.resturantrest.entities;

import com.neshan.resturantrest.models.History;
import com.neshan.resturantrest.models.Restaurant;
import com.neshan.resturantrest.models.User;
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
