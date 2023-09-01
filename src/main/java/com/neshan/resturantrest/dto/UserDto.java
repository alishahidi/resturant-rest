package com.neshan.resturantrest.dto;

import com.neshan.resturantrest.model.History;
import lombok.*;
import lombok.experimental.FieldDefaults;


import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {
    Long id;
    String name;
    String username;
    List<RestaurantDto> restaurants;
    List<HistoryDto> histories;

}
