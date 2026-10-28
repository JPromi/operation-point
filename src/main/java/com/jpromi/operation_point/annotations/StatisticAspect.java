package com.jpromi.operation_point.annotations;

import com.jpromi.operation_point.service.StatisticService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class StatisticAspect {

    private final HttpServletRequest request;

    private final StatisticService statisticService;

    @Around("@annotation(statistic)")
    public Object track(
            ProceedingJoinPoint joinPoint,
            Statistic statistic
    ) throws Throwable {
        statisticService.setStat();

        return joinPoint.proceed();
    }
}
