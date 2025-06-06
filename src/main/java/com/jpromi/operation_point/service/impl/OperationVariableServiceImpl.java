package com.jpromi.operation_point.service.impl;

import com.jpromi.operation_point.service.OperationVariableService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
    public OffsetDateTime getFromAnonymousTime(String datetime) {
        if (datetime == null || datetime.isEmpty()) return null;
        if (datetime.equals("own")) return null;
        String _datetime = datetime.replace('x', '0');
        return OffsetDateTime.of(
                LocalDateTime.parse(_datetime, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")),
                OffsetDateTime.now().getOffset()
        );

    }

    @Override
    public String getAlarmOrganizationTyrol(String alarm) {
        return alarm.split("-")[0];
    }

    @Override
    public String getAlarmOutOrderTyrol(String alarm) {
        String[] parts = alarm.split("-");
        if(parts.length == 3) {
            return parts[1];
        } else {
            return null;
        }
    }

    @Override
    public String getAlarmCategoryTyrol(String alarm) {
        String[] parts = alarm.split("-");
        if(parts.length == 3) {
            return parts[2];
        } else {
            return null;
        }
    }
}
