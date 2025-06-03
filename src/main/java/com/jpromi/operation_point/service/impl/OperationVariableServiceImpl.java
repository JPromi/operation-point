package com.jpromi.operation_point.service.impl;

import com.jpromi.operation_point.service.OperationVariableService;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
public class OperationVariableServiceImpl implements OperationVariableService {

    @Override
    public OffsetDateTime getTimeFromDateAndTime(String date, String time) {
        return null;
    }

    @Override
    public String getAlarmType(String alarmType) {
        if (alarmType != null) {
            return alarmType.replaceAll("\\d", "").trim();
        }
        return null;
    }

    @Override
    public Long getAlarmLevel(String code) {
        if (code != null && !code.isEmpty()) {
            String numericPart = code.replaceAll("\\D", "");
            return numericPart.isEmpty() ? null : Long.parseLong(numericPart);
        }
        return null;
    }

    @Override
    public OffsetDateTime getFromAnonymousTime(String date, String time) {
        return null;
    }
}
