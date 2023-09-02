package com.neshan.resturantrest.controller;

import com.neshan.resturantrest.service.LockService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/lock")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LockController {
    LockService lockService;

    @PutMapping
    public String lock(){
        return lockService.lock();
    }

    @PutMapping("/proper")
    public String properLock(){
        return lockService.properLock();
    }

    @PutMapping("/fail")
    public String failLock(){
        lockService.failLock();
        return "fail lock called, output in logs";
    }
}
