package com.jpromi.operation_point.service;

import java.time.OffsetDateTime;

public interface OperationVariableService {
    OffsetDateTime getTimeFromDateAndTime(String date, String time);
    String getAlarmType(String alarmType);
    Long getAlarmLevel(String code);
    OffsetDateTime getFromAnonymousTime(String date, String time);
}
