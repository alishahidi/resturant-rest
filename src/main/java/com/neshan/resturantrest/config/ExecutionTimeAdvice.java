package com.neshan.resturantrest.config;

import com.neshan.resturantrest.dao.TimeDao;
import com.neshan.resturantrest.model.Time;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

@Aspect
@Component
@ConditionalOnExpression("${aspect.enabled:true}")
@RequiredArgsConstructor
public class ExecutionTimeAdvice {

    private final TimeDao timeDao;

    @Around("@annotation(com.neshan.resturantrest.config.TrackExecutionTime)")
    public Object executionTime(ProceedingJoinPoint point) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object object = point.proceed();
        long endtime = System.currentTimeMillis();
        Time time = Time.builder()
                .totalTime(endtime - startTime)
                .className(point.getSignature().getDeclaringTypeName())
                .methodName(point.getSignature().getName())
                .build();

        timeDao.save(time);
        return object;
    }
}
