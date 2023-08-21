package com.neshan.resturantrest.services;

import com.neshan.resturantrest.models.Food;
import com.neshan.resturantrest.models.History;
import com.neshan.resturantrest.models.Restaurant;
import com.neshan.resturantrest.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryService {
    private List<History> db = new ArrayList<>();

    public History add(String userId, Food food, Restaurant restaurant) {
        History history = History.builder()
                .userId(userId)
                .food(food)
                .restaurant(restaurant)
                .createdAt(new Date())
                .build();

        db.add(history);

        return history;
    }

    public List<History> getHistoryByUserId(String id) {
        return db.stream()
                .filter(history -> history.getUserId().equals(id))
                .toList();
    }
}
