package com.jpromi.operation_point.controller;

import com.jpromi.operation_point.enitiy.CrawlService;
import com.jpromi.operation_point.enitiy.Operation;
import com.jpromi.operation_point.mapper.ServiceStatusResponseMapper;
import com.jpromi.operation_point.model.OperationResponse;
import com.jpromi.operation_point.model.ServiceStatusResponse;
import com.jpromi.operation_point.repository.CrawlServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController("StatusController")
@RequestMapping("/status")
public class StatusController {

    @Autowired
    private ServiceStatusResponseMapper serviceStatusResponseMapper;

    @Autowired
    private CrawlServiceRepository crawlServiceRepository;

    @GetMapping("")
    public ResponseEntity<List<ServiceStatusResponse>> getList() {
        List<CrawlService> services = crawlServiceRepository.findAll();
        List<ServiceStatusResponse> serviceStatusResponse = new ArrayList<>();
        for (CrawlService service : services) {
            ServiceStatusResponse response = serviceStatusResponseMapper.fromCrawlService(service);
            serviceStatusResponse.add(response);
        }
        return ResponseEntity.ok(serviceStatusResponse);
    }

}
