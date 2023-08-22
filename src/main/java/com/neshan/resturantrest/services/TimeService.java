package com.neshan.resturantrest.services;

import com.neshan.resturantrest.models.History;
import com.neshan.resturantrest.models.Time;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TimeService {

    private List<Time> db = new ArrayList<>();

    public Time add(Time time) {
        db.add(time);

        return time;
    }

    public List<Time> get() {
        return db;
    }
}
