package com.jpromi.operation_point.schedule;


import com.jpromi.operation_point.enitiy.CrawlService;
import com.jpromi.operation_point.repository.CrawlServiceRepository;
import com.jpromi.operation_point.service.ApiOperationService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OperationSchedule {

    @Autowired
    private ApiOperationService apiOperationService;

    @Autowired
    private CrawlServiceRepository crawlServiceRepository;

    @Scheduled(fixedRate = 120000) // 120 seconds
    @Transactional
    public void runUpdateOperations() {
        if(isServiceActive("fs-ua")) updateUpperAustria(); else System.out.println("Upper Austria service is not active.");
        if(isServiceActive("fs-la")) updateLowerAustria(); else System.out.println("Lower Austria service is not active.");
        if(isServiceActive("fs-st")) updateStyria(); else System.out.println("Styria service is not active.");
        if(isServiceActive("fs-ty")) updateTyrol(); else System.out.println("Tyrol service is not active.");
        if(isServiceActive("fs-bl")) updateBurgenland(); else System.out.println("Burgenland service is not active.");
    }

    private void updateTyrol() {
        System.out.println("Updating Tyrol operations...");
        try {
            apiOperationService.getOperationListTyrol();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateUpperAustria() {
        System.out.println("Updating Upper Austria operations...");
        try {
            apiOperationService.getOperationListUpperAustria();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateStyria() {
        System.out.println("Updating Styria operations...");
        try {
            apiOperationService.getOperationListStyria();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateBurgenland() {
        System.out.println("Updating Burgenland operations...");
        try {
            apiOperationService.getOperationListBurgenland();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateLowerAustria() {
        System.out.println("Updating Lower Austria operations...");
        try {
            apiOperationService.getOperationListLowerAustria();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Boolean isServiceActive(String serviceName) {
        CrawlService crawlService = crawlServiceRepository.findByName(serviceName);
        return crawlService != null && crawlService.getIsEnabled();
    }

}
