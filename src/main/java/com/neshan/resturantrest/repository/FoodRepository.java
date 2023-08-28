package com.neshan.resturantrest.repository;

import com.neshan.resturantrest.model.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {
    public List<Food> findByRestaurantId(Long restaurantId);
}
