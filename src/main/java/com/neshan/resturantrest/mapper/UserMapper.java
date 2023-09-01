package com.neshan.resturantrest.mapper;

import com.neshan.resturantrest.dto.UserDto;
import com.neshan.resturantrest.model.User;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserMapper implements Function<User, UserDto> {

    RestaurantMapper restaurantMapper;
    HistoryMapper historyMapper;

    @Override
    public UserDto apply(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .username(user.getUsername())
                .histories(
                        Optional.ofNullable(user.getHistories())
                                .orElse(Collections.emptyList())
                                .stream()
                                .map(historyMapper).toList()
                )
                .restaurants(
                        Optional.ofNullable(user.getRestaurants())
                                .orElse(Collections.emptyList())
                                .stream()
                                .map(restaurantMapper).toList()
                )
                .build();
    }
}
