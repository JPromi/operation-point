package com.jpromi.operation_point.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpromi.operation_point.service.OperationVariableService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class OperationVariableServiceImpl implements OperationVariableService {

    @Override
    public OffsetDateTime getTimeFromDateAndTime(String date, String time) {
        if (date == null || time == null || date.isEmpty() || time.isEmpty()) {
            return null;
        }
        String datetime = date + " " + time;
        return OffsetDateTime.of(
                LocalDateTime.parse(datetime, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")),
                OffsetDateTime.now().getOffset()
        );
    }

    @Override
    public String getAlarmType(String alarmType) {
        if (alarmType == null) return null;
        var matcher = java.util.regex.Pattern.compile("^[A-Z]+(?=\\d)").matcher(alarmType);
        if (matcher.find()) {
            return matcher.group();
        } else if (alarmType.matches("^[A-Z]+$")) {
            return alarmType; // nur Großbuchstaben, keine Zahl → direkt zurückgeben
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
    public String getAlarmLevelAddition(String alarmType) {
        if (alarmType == null) return null;
        var matcher = java.util.regex.Pattern.compile("(?<=\\d)[A-Z]+$").matcher(alarmType);
        return matcher.find() ? matcher.group() : null;
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

    @Override
    public String getFederalState(String federalState) {
        federalState = federalState.toLowerCase();
        federalState = federalState.replaceAll(" ", "-");
        switch (federalState) {
            case "ua", "upper-austria":
                return "Upper Austria";
            case "st", "styria":
                return "Styria";
            case "ty", "tyrol":
                return "Tyrol";
            case "la", "lower-austria":
                return "Lower Austria";
            case "bg", "burgenland", "bl":
                return "Burgenland";
            default:
                return federalState;
        }
    }

    @Override
    public String getDistrict(String districtId) {
        if(districtId == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("mapping/district-mapping.json")) {
            if (is == null) {
                throw new IllegalStateException("File not found: mapping/district-mapping.json");
            }

            // list of districts
            List<Map<String, String>> districtList = mapper.readValue(is, new TypeReference<List<Map<String, String>>>() {});

            // search for the district by ID
            for (Map<String, String> entry : districtList) {
                if (districtId.equals(entry.get("id"))) {
                    return entry.get("name");
                }
            }

            throw new IllegalArgumentException("District ID not found: " + districtId);
        } catch (IOException e) {
            throw new RuntimeException("Error reading district-mapping.json", e);
        }
    }

    @Override
    public String getDistrictId(String district) {
        if(district == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("mapping/district-mapping.json")) {
            if (is == null) {
                throw new IllegalStateException("File not found: mapping/district-mapping.json");
            }

            // list of districts
            List<Map<String, String>> districtList = mapper.readValue(is, new TypeReference<List<Map<String, String>>>() {});

            // search for the district by name
            for (Map<String, String> entry : districtList) {
                if (district.equalsIgnoreCase(entry.get("name"))) {
                    return entry.get("id");
                }
            }

            throw new IllegalArgumentException("District not found: " + district);
        } catch (IOException e) {
            throw new RuntimeException("Error reading district-mapping.json", e);
        }
    }
}
