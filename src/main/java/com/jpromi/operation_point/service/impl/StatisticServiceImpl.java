package com.jpromi.operation_point.service.impl;

import com.jpromi.operation_point.entity.Statistic;
import com.jpromi.operation_point.repository.StatisticRepository;
import com.jpromi.operation_point.service.StatisticService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class StatisticServiceImpl implements StatisticService {

    private final HttpServletRequest request;
    private final StatisticRepository statisticRepository;

    @Autowired
    public StatisticServiceImpl(
            HttpServletRequest request,
            StatisticRepository statisticRepository
    ) {
        this.request = request;
        this.statisticRepository = statisticRepository;
    }
    @Override
    @Async("statisticsExecutor")
    public Void setStat() {
        Statistic stat = generateBaseObject();
        statisticRepository.save(stat);
        return null;
    }

    @Override
    public Void setStatForDetail(String objectType, UUID objectUuid) {
        Statistic stat = generateBaseObject();
        stat.setObjectType(objectType);
        stat.setObjectUuid(objectUuid);
        statisticRepository.save(stat);
        return null;
    }

    private Statistic generateBaseObject() {
        return Statistic.builder()
                .appVersion(request.getHeader("X-Fw-Version"))
                .platform(request.getHeader("X-Fw-Platform"))
                .userAgent(request.getHeader("User-Agent"))
                .requestOrigin(request.getHeader("Origin"))
                .requestReferer(request.getHeader("Referer"))
                .path(request.getRequestURI())
                .build();
    }
}
