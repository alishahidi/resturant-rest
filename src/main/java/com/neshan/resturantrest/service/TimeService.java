package com.neshan.resturantrest.service;

import com.neshan.resturantrest.model.Time;
import com.neshan.resturantrest.repository.TimeRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TimeService {

    TimeRepository timeRepository;

    public Time add(Time time) {
        Time createdTime = timeRepository.save(time);

        return createdTime;
    }

    public List<Time> get() {
        return timeRepository.findAll();
    }
}
