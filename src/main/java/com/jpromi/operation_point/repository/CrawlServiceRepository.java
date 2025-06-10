package com.jpromi.operation_point.repository;

import com.jpromi.operation_point.enitiy.CrawlService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CrawlServiceRepository extends JpaRepository<CrawlService, Long> {
    CrawlService findByName(String name);
}
