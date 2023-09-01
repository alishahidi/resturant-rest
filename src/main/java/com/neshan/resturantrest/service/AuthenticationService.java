package com.neshan.resturantrest.service;

import com.neshan.resturantrest.dto.UserDto;
import com.neshan.resturantrest.enums.Role;
import com.neshan.resturantrest.mapper.UserMapper;
import com.neshan.resturantrest.model.User;
import com.neshan.resturantrest.repository.UserRepository;
import com.neshan.resturantrest.request.AuthenticationRequest;
import com.neshan.resturantrest.request.RegisterRequest;
import com.neshan.resturantrest.dto.AuthenticationDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {

    PasswordEncoder passwordEncoder;
    JwtService jwtService;
    AuthenticationManager authenticationManager;
    UserRepository userRepository;
    UserMapper userMapper;

    public AuthenticationDto register(RegisterRequest request) {
        User user = User
                .builder()
                .name(request.getName())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        User createdUser = userRepository.save(user);
        String jwtToken = jwtService.generateToken(user);

        return AuthenticationDto
                .builder()
                .token(jwtToken)
                .user(userMapper.apply(createdUser))
                .build();
    }

    public AuthenticationDto authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));
        String jwtToken = jwtService.generateToken(user);

        return AuthenticationDto
                .builder()
                .token(jwtToken)
                .user(userMapper.apply(user))
                .build();
    }

    public UserDto get() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return userMapper.apply(user);
    }
}
