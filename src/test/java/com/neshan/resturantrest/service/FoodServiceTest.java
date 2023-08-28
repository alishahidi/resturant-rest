package com.neshan.resturantrest.service;

import com.neshan.resturantrest.model.Food;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class FoodServiceTest {

    @Mock
    private ArrayList<Food> foodRepository;
    @Mock
    private RestaurantService restaurantService;

    @InjectMocks
    private FoodService foodService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
//        when(foodRepository.stream()).thenReturn(Stream.of(
//                new Food("1", "Water", 100.0, "1", 5),
//                new Food("2", "Milk", 100.0, "2", 2)
//        ));
        List<Food> db = new ArrayList<>();
        db.add(new Food("1", "Water", 100.0, "1", 5));
        db.add(new Food("2", "Milk", 100.0, "2", 2));
        foodService.setDb(db);
    }

    @Test
    void getTest() {
        Food expectedFood = new Food("1", "Water", 100.0, "1", 5);
        Food result = foodService.get("1");

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(expectedFood);
    }

    @Test
    void getNotExistFoodTest() {
        assertThatThrownBy(() -> foodService.get("notExist")).isInstanceOf(RuntimeException.class);
    }

    @Test
    void addFoodTest() {
        String restaurantId = UUID.randomUUID().toString();
        Food newFood = Food.builder()
                .name("New Food")
                .price(15.0)
                .quantity(10)
                .build();
        Food addedFood = foodService.addFood(newFood, restaurantId);

        assertThat(addedFood.getName()).isEqualTo(newFood.getName());
        assertThat(addedFood.getPrice()).isEqualTo(newFood.getPrice());
        assertThat(addedFood.getQuantity()).isEqualTo(newFood.getQuantity());
        assertThat(addedFood.getRestaurantId()).isEqualTo(restaurantId);

        List<Food> foods = foodService.getDb();
        assertThat(foods).contains(addedFood);
    }

    @Test
    void updateFoodWithPriceNotNullTest() {
        Food expectedFood = new Food("1", "Water", 300.0, "1", 5);
        Food changedFood = Food.builder()
                .id("1")
                .price(300.0)
                .build();

        Food updatedFood = foodService.updateFood(changedFood);

        assertThat(updatedFood.getName()).isEqualTo(expectedFood.getName());
        assertThat(updatedFood.getPrice()).isEqualTo(expectedFood.getPrice());
        assertThat(updatedFood.getQuantity()).isEqualTo(expectedFood.getQuantity());
        assertThat(updatedFood.getRestaurantId()).isEqualTo(expectedFood.getRestaurantId());
    }

    @Test
    void updateFoodWithNameNotNullTest() {
        Food expectedFood = new Food("1", "Water 2", 100.0, "1", 5);
        Food changedFood = Food.builder()
                .id("1")
                .name("Water 2")
                .build();

        Food updatedFood = foodService.updateFood(changedFood);

        assertThat(updatedFood.getName()).isEqualTo(expectedFood.getName());
        assertThat(updatedFood.getPrice()).isEqualTo(expectedFood.getPrice());
        assertThat(updatedFood.getQuantity()).isEqualTo(expectedFood.getQuantity());
        assertThat(updatedFood.getRestaurantId()).isEqualTo(expectedFood.getRestaurantId());
    }

    @Test
    void updateFoodWithQuantityNotNullTest() {
        Food expectedFood = new Food("1", "Water", 100.0, "1", 3);
        Food changedFood = Food.builder()
                .id("1")
                .quantity(3)
                .build();

        Food updatedFood = foodService.updateFood(changedFood);

        assertThat(updatedFood.getName()).isEqualTo(expectedFood.getName());
        assertThat(updatedFood.getPrice()).isEqualTo(expectedFood.getPrice());
        assertThat(updatedFood.getQuantity()).isEqualTo(expectedFood.getQuantity());
        assertThat(updatedFood.getRestaurantId()).isEqualTo(expectedFood.getRestaurantId());
    }
}