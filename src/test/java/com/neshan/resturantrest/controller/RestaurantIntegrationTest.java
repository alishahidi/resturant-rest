package com.neshan.resturantrest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neshan.resturantrest.enums.Role;
import com.neshan.resturantrest.model.Restaurant;
import com.neshan.resturantrest.model.User;
import com.neshan.resturantrest.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class RestaurantIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;

    @Mock
    private Authentication authentication;

    @Autowired
    private ObjectMapper objectMapper;

    User user = User.builder()
            .id(4L)
            .name("Ali Shahidi")
            .role(Role.ADMIN)
            .password("testpass")
            .username("alishahidi")
            .build();

    @Test
    @Sql("/user.sql")
    @WithMockUser(username = "alishahidi")
    void createRestaurant() throws Exception {
        Mockito.when(authentication.getPrincipal()).thenReturn(user);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Restaurant restaurant = Restaurant.builder()
                .name("Ali")
                .address("sddsdsd")
                .build();

        String requestJson = objectMapper.writeValueAsString(restaurant);


        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/restaurant/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}
