package com.jpromi.operation_point.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ApiOperationBurgenlandResponse {
    private List<ApiOperationBurgenlandResponseOperation> operations;

    @Data
    public static class ApiOperationBurgenlandResponseOperation {
        private Long id;

        @JsonProperty("place_of_operation")
        private String placeOfOperation;

        private String code;

        @JsonProperty("start_time")
        private Long startTime;


        @JsonProperty("end_time")
        private Long endTime;

        private String district;

        @JsonProperty("num_fire_services")
        private Long numFireServices;

        @JsonProperty("num_vehicles")
        private Long numVehicles;

        @JsonProperty("fire_services")
        private List<String> fireServices;

        private Boolean info;
    }
}
