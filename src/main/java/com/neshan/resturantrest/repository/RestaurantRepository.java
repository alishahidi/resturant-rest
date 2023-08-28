package com.neshan.resturantrest.repository;

import com.neshan.resturantrest.model.Restaurant;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    @Transactional
    @Modifying
    @Query("DELETE FROM Restaurant r WHERE r.id = :restaurantId")
    void deleteRestaurantById(Long restaurantId);
}
