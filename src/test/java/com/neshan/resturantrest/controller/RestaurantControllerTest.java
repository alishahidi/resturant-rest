package com.neshan.resturantrest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neshan.resturantrest.model.Food;
import com.neshan.resturantrest.model.History;
import com.neshan.resturantrest.model.Restaurant;
import com.neshan.resturantrest.model.User;
import com.neshan.resturantrest.service.FoodService;
import com.neshan.resturantrest.service.HistoryService;
import com.neshan.resturantrest.service.RestaurantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class RestaurantControllerTest {

    @MockBean
    private RestaurantService restaurantService;
    @MockBean
    private FoodService foodService;
    @MockBean
    private HistoryService historyService;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private Authentication authentication;

    @Autowired
    private MockMvc mockMvc;

    private final List<Restaurant> restaurants = List.of(

            new Restaurant("2", "1", "Ali Restaurant 2", "Tehran")
    );

    private final List<Food> menu = List.of(
            new Food("1", "Food 1", 10.0, "1", 5),
            new Food("2", "Food 2", 15.0, "1", 8)
    );

    @BeforeEach
    void setUp() {
        when(restaurantService.get()).thenReturn(restaurants);
    }

    @Test
    void getTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/restaurant")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(restaurants.size()))
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].name").value("Ali Restaurant"));
    }

    @Test
    void getRestaurantByIdTest() throws Exception {
        when(foodService.getFoodsByRestaurantId("1")).thenReturn(menu);
        when(restaurantService.get("1")).thenReturn(restaurants.get(0));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/restaurant/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.restaurant.id").value("1"))
                .andExpect(jsonPath("$.restaurant.name").value("Ali Restaurant"))
                .andExpect(jsonPath("$.menu.length()").value(menu.size()))
                .andExpect(jsonPath("$.menu[0].id").value("1"))
                .andExpect(jsonPath("$.menu[0].name").value("Food 1"));
    }

    @Test
    void createRestaurantTest() throws Exception {
        when(authentication.getPrincipal()).thenReturn(new User("4", "Ali Shahidi", "ali", "12345678"));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Restaurant newRestaurant = Restaurant.builder()
                .address("Mashhad")
                .id("4")
                .ownerId("4")
                .name("New Restaurant")
                .build();
        when(restaurantService.add(ArgumentMatchers.any(Restaurant.class), ArgumentMatchers.eq("4")))
                .thenReturn(newRestaurant);

        String requestJson = objectMapper.writeValueAsString(newRestaurant);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/restaurant/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("4"))
                .andExpect(jsonPath("$.ownerId").value("4"))
                .andExpect(jsonPath("$.name").value("New Restaurant"))
                .andExpect(jsonPath("$.address").value("Mashhad"));
    }

    @Test
    void deleteRestaurantTest() throws Exception {
        when(authentication.getPrincipal()).thenReturn(new User("4", "Ali Shahidi", "ali", "12345678"));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Restaurant restaurant = new Restaurant("6", "4", "Test Restaurant", "Mashhad");
        when(restaurantService.get("6")).thenReturn(restaurant);
        when(foodService.getFoodsByRestaurantId("6")).thenReturn(List.of(
                new Food("6", "Food 1", 10.0, "6", 5),
                new Food("7", "Food 2", 15.0, "6", 8),
                new Food("8", "Food 3", 10.0, "6", 5),
                new Food("9", "Food 4", 15.0, "6", 8)
        ));

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/restaurant/{id}", "6")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.restaurant.id").value("6"))
                .andExpect(jsonPath("$.restaurant.name").value("Test Restaurant"))
                .andExpect(jsonPath("$.restaurant.address").value("Mashhad"));

        verify(foodService, times(4)).deleteFood(any(Food.class));
    }

    @Test
    void addFoodTest() throws Exception {
        when(authentication.getPrincipal()).thenReturn(new User("4", "Ali Shahidi", "ali", "12345678"));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Restaurant restaurant = new Restaurant("6", "4", "Test Restaurant", "Mashhad");
        when(restaurantService.get("6")).thenReturn(restaurant);

        Food newFood = new Food("6", "Food 1", 10.0, "6", 5);
        when(foodService.addFood(ArgumentMatchers.any(Food.class), ArgumentMatchers.eq("6")))
                .thenReturn(newFood);

        String requestJson = objectMapper.writeValueAsString(newFood);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/restaurant/food/{restaurantId}", "6")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("6"))
                .andExpect(jsonPath("$.name").value("Food 1"))
                .andExpect(jsonPath("$.restaurantId").value("6"));
    }

    @Test
    void addForbiddenFoodTest() throws Exception {
        when(authentication.getPrincipal()).thenReturn(new User("4", "Ali Shahidi", "ali", "12345678"));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Restaurant restaurant = new Restaurant("6", "5", "Test Restaurant", "Mashhad");
        when(restaurantService.get("6")).thenReturn(restaurant);

        Food newFood = new Food("6", "Food 1", 10.0, "6", 5);
        when(foodService.addFood(ArgumentMatchers.any(Food.class), ArgumentMatchers.eq("6")))
                .thenReturn(newFood);

        String requestJson = objectMapper.writeValueAsString(newFood);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/restaurant/food/{restaurantId}", "6")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isForbidden());
    }

    @Test
    void serveFoodTest() throws Exception {
        when(authentication.getPrincipal()).thenReturn(new User("4", "Ali Shahidi", "ali", "12345678"));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Food food = new Food("7", "Pizza", 10.0, "6", 2);
        when(foodService.get("7")).thenReturn(food);

        Restaurant restaurant = new Restaurant("6", "4", "Test Restaurant", "Mashhad");
        when(restaurantService.get("6")).thenReturn(restaurant);

        History history = new History(food, "4", restaurant, new Date());
        when(historyService.add("4", food, restaurant)).thenReturn(history);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/restaurant/food/serve/{foodId}/{restaurantId}", "7", "6"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.restaurant.id").value("6"))
                .andExpect(jsonPath("$.food.name").value("Pizza"))
                .andExpect(jsonPath("$.food.quantity").value(1))
        ;
    }

    @Test
    void serveOwnerIdForbiddenFoodTest() throws Exception {
        when(authentication.getPrincipal()).thenReturn(new User("4", "Ali Shahidi", "ali", "12345678"));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Food food = new Food("7", "Pizza", 10.0, "5", 2);
        when(foodService.get("7")).thenReturn(food);

        Restaurant restaurant = new Restaurant("6", "4", "Test Restaurant", "Mashhad");
        when(restaurantService.get("6")).thenReturn(restaurant);

        History history = new History(food, "4", restaurant, new Date());
        when(historyService.add("4", food, restaurant)).thenReturn(history);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/restaurant/food/serve/{foodId}/{restaurantId}", "7", "6"))
                .andExpect(status().isForbidden());
    }

    @Test
    void serveFoodQuantityForbiddenTest() throws Exception {
        when(authentication.getPrincipal()).thenReturn(new User("4", "Ali Shahidi", "ali", "12345678"));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Food food = new Food("7", "Pizza", 10.0, "6", 0);
        when(foodService.get("7")).thenReturn(food);

        Restaurant restaurant = new Restaurant("6", "4", "Test Restaurant", "Mashhad");
        when(restaurantService.get("6")).thenReturn(restaurant);

        History history = new History(food, "4", restaurant, new Date());
        when(historyService.add("4", food, restaurant)).thenReturn(history);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/restaurant/food/serve/{foodId}/{restaurantId}", "7", "6"))
                .andExpect(status().isForbidden());
    }

    @Test
    void updateFoodTest() throws Exception {
        when(authentication.getPrincipal()).thenReturn(new User("4", "Ali Shahidi", "ali", "12345678"));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Food existingFood = new Food("9", "Pizza", 10.0, "6", 5);
        when(foodService.get("9")).thenReturn(existingFood);

        Restaurant restaurant = new Restaurant("6", "4", "Test Restaurant", "Mashhad");
        when(restaurantService.get("6")).thenReturn(restaurant);

        Food updatedFood = new Food("9", "Updated Pizza", 12.0, "6", 4);
        when(foodService.updateFood(ArgumentMatchers.any(Food.class))).thenReturn(updatedFood);

        String requestJson = objectMapper.writeValueAsString(updatedFood);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/restaurant/food/{id}/{restaurantId}", "9", "6")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("9"))
                .andExpect(jsonPath("$.name").value("Updated Pizza"));
    }

    @Test
    void updateRestaurantIdForbiddenFoodTest() throws Exception {
        when(authentication.getPrincipal()).thenReturn(new User("4", "Ali Shahidi", "ali", "12345678"));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Food existingFood = new Food("9", "Pizza", 10.0, "6", 5);
        when(foodService.get("9")).thenReturn(existingFood);

        Restaurant restaurant = new Restaurant("6", "4", "Test Restaurant", "Mashhad");
        when(restaurantService.get("6")).thenReturn(restaurant);

        Food updatedFood = new Food("9", "Updated Pizza", 12.0, "6", 4);
        when(foodService.updateFood(ArgumentMatchers.any(Food.class))).thenReturn(updatedFood);

        String requestJson = objectMapper.writeValueAsString(updatedFood);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/restaurant/food/{id}/{restaurantId}", "9", "7")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isForbidden());
    }

    @Test
    void updateOwnerIdForbiddenFoodTest() throws Exception {
        when(authentication.getPrincipal()).thenReturn(new User("4", "Ali Shahidi", "ali", "12345678"));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Food existingFood = new Food("9", "Pizza", 10.0, "6", 5);
        when(foodService.get("9")).thenReturn(existingFood);

        Restaurant restaurant = new Restaurant("6", "6", "Test Restaurant", "Mashhad");
        when(restaurantService.get("6")).thenReturn(restaurant);

        Food updatedFood = new Food("9", "Updated Pizza", 12.0, "6", 4);
        when(foodService.updateFood(ArgumentMatchers.any(Food.class))).thenReturn(updatedFood);

        String requestJson = objectMapper.writeValueAsString(updatedFood);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/restaurant/food/{id}/{restaurantId}", "9", "6")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteFoodTest() throws Exception {
        when(authentication.getPrincipal()).thenReturn(new User("4", "Ali Shahidi", "ali", "12345678"));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Food existingFood = new Food("10", "Pizza", 10.0, "6", 5);
        when(foodService.get("10")).thenReturn(existingFood);

        Restaurant restaurant = new Restaurant("6", "4", "Test Restaurant", "Mashhad");
        when(restaurantService.get("6")).thenReturn(restaurant);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/restaurant/food/{id}/{restaurantId}", "10", "6"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}