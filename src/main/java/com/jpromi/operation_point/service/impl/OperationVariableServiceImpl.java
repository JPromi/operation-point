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
        return null;
    }

    @Override
    public Long getAlarmLevel(String code) {
        return null;
    }

    @Override
    public OffsetDateTime getFromAnonymousTime(String date, String time) {
        return null;
    }
}
