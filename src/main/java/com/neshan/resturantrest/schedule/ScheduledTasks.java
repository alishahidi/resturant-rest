package com.neshan.resturantrest.schedule;

import com.neshan.resturantrest.dao.TimeDao;
import com.neshan.resturantrest.model.Time;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;


import java.util.List;

@Component
@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
@Slf4j
public class ScheduledTasks {
     TimeDao timeDao;

    @Scheduled(cron = "0 */5 * * * *")
    public void reportAverageTime() throws InterruptedException {
        List<Time> times = timeDao.findAll();

        log.info("The average took time is: " +
                times.stream()
                        .mapToLong(Time::getTotalTime)
                        .average()
                        .orElseGet(() -> 0.0)
        );

        timeDao.removeAll();
    }

}
