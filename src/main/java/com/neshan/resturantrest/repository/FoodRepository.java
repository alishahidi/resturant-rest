package com.neshan.resturantrest.repository;

import com.neshan.resturantrest.model.Food;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {
    @Transactional
    @Modifying
    @Query("DELETE FROM Food f WHERE f.restaurant.id = :restaurantId")
    void deleteFoodsByRestaurantId(Long restaurantId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Food f WHERE f.id = :id")
    void deleteFoodById(Long id);
}
