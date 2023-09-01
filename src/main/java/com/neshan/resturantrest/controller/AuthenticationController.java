package com.neshan.resturantrest.controller;

import com.neshan.resturantrest.dto.AuthenticationDto;
import com.neshan.resturantrest.dto.UserDto;
import com.neshan.resturantrest.request.AuthenticationRequest;
import com.neshan.resturantrest.request.RegisterRequest;
import com.neshan.resturantrest.service.AuthenticationService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {

    AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationDto> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationDto> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @GetMapping
    @Transactional
    public ResponseEntity<UserDto> get() {
        return ResponseEntity.ok(authenticationService.get());
    }
}
