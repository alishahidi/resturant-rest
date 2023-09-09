package com.neshan.resturantrest.dao;

import com.neshan.resturantrest.model.Time;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TimeDao {

    private String HASH_KEY = "Time";
    RedissonClient redisson;

    public void save(Time time) {
        String id = UUID.randomUUID().toString();
        time.setId(id);

        RMap<String, Time> map = redisson.getMap(HASH_KEY);
        map.put(id, time);
    }

    public List<Time> findAll() {
        RMap<String, Time> map = redisson.getMap(HASH_KEY);
        return new ArrayList<>(map.values());
    }

    public void removeAll() {
        RMap<String, Time> map = redisson.getMap(HASH_KEY);
        map.clear();
    }

}
