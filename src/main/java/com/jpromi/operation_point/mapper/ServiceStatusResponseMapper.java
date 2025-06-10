package com.jpromi.operation_point.mapper;

import com.jpromi.operation_point.enitiy.CrawlService;
import com.jpromi.operation_point.model.ServiceStatusResponse;
import org.springframework.stereotype.Component;

@Component
public class ServiceStatusResponseMapper {

    public ServiceStatusResponse fromCrawlService(CrawlService crawlService) {
        return ServiceStatusResponse.builder()
                .serviceName(crawlService.getName())
                .name(crawlService.getFriendlyName())
                .isEnabled(crawlService.getIsEnabled())
                .build();
    }
}
