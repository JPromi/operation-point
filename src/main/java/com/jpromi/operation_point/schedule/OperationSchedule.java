package com.jpromi.operation_point.schedule;


import com.jpromi.operation_point.entity.CrawlService;
import com.jpromi.operation_point.repository.CrawlServiceRepository;
import com.jpromi.operation_point.service.ApiOperationService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OperationSchedule {
    private final ApiOperationService apiOperationService;
    private final CrawlServiceRepository crawlServiceRepository;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public OperationSchedule(ApiOperationService apiOperationService, CrawlServiceRepository crawlServiceRepository) {
        this.apiOperationService = apiOperationService;
        this.crawlServiceRepository = crawlServiceRepository;
    }

    @Scheduled(fixedRate = 120000) // 120 seconds
    @Transactional
    public void runUpdateOperations() {
        if(isServiceActive("fs-ua")) {
            updateUpperAustria();
        } else {
            logger.warn("Upper Austria service is not active.");
        }

        if(isServiceActive("fs-la")) {
            updateLowerAustria();
        } else {
            logger.warn("Lower Austria service is not active.");
        }
        
        if(isServiceActive("fs-st")) {
            updateStyria();
        } else {
            logger.warn("Styria service is not active.");
        }
        
        if(isServiceActive("fs-ty")) {
            updateTyrol();
        } else {
            logger.warn("Tyrol service is not active.");
        }
        
        if(isServiceActive("fs-bl")) {
            updateBurgenland();
        } else {
            logger.warn("Burgenland service is not active.");
        }
    }

    private void updateTyrol() {
        logger.info("Updating Tyrol operations...");
        try {
            apiOperationService.getOperationListTyrol();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    private void updateUpperAustria() {
        logger.info("Updating Upper Austria operations...");
        try {
            apiOperationService.getOperationListUpperAustria();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    private void updateStyria() {
        logger.info("Updating Styria operations...");
        try {
            apiOperationService.getOperationListStyria();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    private void updateBurgenland() {
        logger.info("Updating Burgenland operations...");
        try {
            apiOperationService.getOperationListBurgenland();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    private void updateLowerAustria() {
        logger.info("Updating Lower Austria operations...");
        try {
            apiOperationService.getOperationListLowerAustria();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    private Boolean isServiceActive(String serviceName) {
        CrawlService crawlService = crawlServiceRepository.findByName(serviceName);
        return crawlService != null && crawlService.getIsEnabled();
    }

}
