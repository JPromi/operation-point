package com.jpromi.operation_point.schedule;


import com.jpromi.operation_point.service.ApiOperationService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OperationSchedule {

    @Autowired
    private ApiOperationService apiOperationService;

    @Scheduled(fixedRate = 120000) // 120 seconds
    @Transactional
    public void runUpdateOperations() {
        updateUpperAustria();
        updateLowerAustria();
        updateStyria();
        updateTyrol();
        updateBurgenland();
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

}
