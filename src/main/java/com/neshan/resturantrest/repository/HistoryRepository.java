package com.neshan.resturantrest.repository;

import com.neshan.resturantrest.model.History;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {
    @Transactional
    @Modifying
    @Query("DELETE FROM History h WHERE h.food.id = :foodId")
    void deleteHistoriesByFoodId(Long foodId);

    @Transactional
    @Modifying
    @Query("DELETE FROM History h WHERE h.restaurant.id = :restaurantId")
    void deleteHistoriesByRestaurantId(Long restaurantId);
}
