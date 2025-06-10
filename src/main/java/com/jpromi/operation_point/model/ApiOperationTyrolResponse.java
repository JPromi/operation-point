package com.jpromi.operation_point.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ApiOperationTyrolResponse {

    @JsonProperty("NAME")
    private String name;

    @JsonProperty("REMARK")
    private String remark;

    @JsonProperty("NAME_AT_ALARMTIME")
    private List<String> nameAtAlarmTime;

    @JsonProperty("CITY")
    private String city;

    @JsonProperty("ZIPCODE")
    private String zipcode;

    @JsonProperty("ALARMTIME")
    private String alarmtime;

    @JsonProperty("ALARMTIME_FORMATTED")
    private String alarmtimeFormatted;

    @JsonProperty("LAT")
    private Double lat;

    @JsonProperty("LON")
    private Double lon;

    @JsonProperty("EVENTNUM")
    private String eventnum;

    @JsonProperty("STATUS")
    private String status;
}
