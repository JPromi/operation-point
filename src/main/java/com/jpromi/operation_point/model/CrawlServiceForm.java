package com.jpromi.operation_point.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
public class CrawlServiceForm {
    private List<CrawlServiceFormService> services;

    @Getter
    @Setter
    public static class CrawlServiceFormService {
        private String name;
        private Boolean isEnabled = false;
    }
}
