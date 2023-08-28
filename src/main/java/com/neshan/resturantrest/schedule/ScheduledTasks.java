package com.neshan.resturantrest.schedule;

import com.neshan.resturantrest.model.Time;
import com.neshan.resturantrest.service.TimeService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Getter
@RequiredArgsConstructor
public class ScheduledTasks {

    private final TimeService timeService;
    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);


    @Scheduled(cron = "5 * * * * ?")
    public void reportAverageTime() throws InterruptedException {
        List<Time> times = timeService.get();

        log.info("The average took time is: " +
                times.stream()
                        .mapToLong(Time::getTotalTime)
                        .average()
                        .orElseGet(() -> 0.0)
        );
    }

}
