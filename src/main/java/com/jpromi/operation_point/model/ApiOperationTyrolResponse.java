package com.jpromi.operation_point.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ApiOperationTyrolResponse {

    @JsonProperty("DATA")
    private List<Operation> data;

    private Long generationTimeStamp;

    @Data
    public static class Operation {

        @JsonProperty("ID")
        private String id;

        @JsonProperty("NAME_AT_ALARMTIME")
        private String nameAtAlarmTime;

        @JsonProperty("NAMEEVENTTYPE")
        private String nameEventType;

        @JsonProperty("STATUS")
        private String status; // finished, alarmed

        @JsonProperty("ALARMTIME")
        private String alarmtime;

        @JsonProperty("INFO")
        private String info;

        @JsonProperty("ZIPCODE")
        private String zipcode;

        @JsonProperty("CITY")
        private String city;
    }
}
